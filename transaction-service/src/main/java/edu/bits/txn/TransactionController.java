package edu.bits.txn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private static final BigDecimal DAILY_LIMIT = new BigDecimal("200000");
    private final TransactionRepository transactionRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Timer balanceCheckLatency;
    private final Counter transactionsTotal;
    private final Counter failedTransfersTotal;

    @Value("${ACCOUNT_SERVICE_BASE_URL:http://localhost:8082}")
    private String accountServiceBaseUrl;

    public TransactionController(
            TransactionRepository transactionRepository,
            IdempotencyKeyRepository idempotencyKeyRepository,
            RestTemplate restTemplate,
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper,
            MeterRegistry meterRegistry) {
        this.transactionRepository = transactionRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.balanceCheckLatency = meterRegistry.timer("balance_check_latency_ms");
        this.transactionsTotal = meterRegistry.counter("transactions_total");
        this.failedTransfersTotal = meterRegistry.counter("failed_transfers_total");
    }

    @PostMapping("/transfer")
    @Transactional
    public Map<String, Object> transfer(@RequestBody TransferRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        IdempotencyKey existing = idempotencyKeyRepository.findByKeyValue(idempotencyKey).orElse(null);
        if (existing != null) {
            return Map.of("status", "REPLAY", "payload", existing.getResponsePayload());
        }

        try {
            validateInrTransferOnly(request.fromAccountNumber(), request.toAccountNumber());
            enforceDailyLimit(request);
            timedAccountCall(request.fromAccountNumber(), "/debit?amount=" + request.amount(), HttpMethod.POST);
            try {
                timedAccountCall(request.toAccountNumber(), "/credit?amount=" + request.amount(), HttpMethod.POST);
            } catch (RuntimeException e) {
                timedAccountCall(request.fromAccountNumber(), "/credit?amount=" + request.amount(), HttpMethod.POST);
                throw e;
            }

            String correlationId = UUID.randomUUID().toString();
            TransactionRecord debit = new TransactionRecord();
            debit.setAccountNumber(request.fromAccountNumber());
            debit.setAmount(request.amount());
            debit.setTxnType("DEBIT");
            debit.setCounterparty(request.toAccountNumber());
            debit.setReference(request.reference());
            debit.setCorrelationId(correlationId);
            transactionRepository.save(debit);

            TransactionRecord credit = new TransactionRecord();
            credit.setAccountNumber(request.toAccountNumber());
            credit.setAmount(request.amount());
            credit.setTxnType("CREDIT");
            credit.setCounterparty(request.fromAccountNumber());
            credit.setReference(request.reference());
            credit.setCorrelationId(correlationId);
            transactionRepository.save(credit);

            Map<String, Object> payload = Map.of(
                    "eventType", "TransactionCreated",
                    "correlationId", correlationId,
                    "fromAccount", request.fromAccountNumber(),
                    "toAccount", request.toAccountNumber(),
                    "amount", request.amount(),
                    "reference", request.reference());
            rabbitTemplate.convertAndSend("txn.exchange", "txn.created", payload);

            IdempotencyKey key = new IdempotencyKey();
            key.setKeyValue(idempotencyKey);
            key.setResponsePayload(toJson(payload));
            idempotencyKeyRepository.save(key);
            transactionsTotal.increment();
            return payload;
        } catch (RuntimeException ex) {
            failedTransfersTotal.increment();
            throw ex;
        }
    }

    @GetMapping("/account/{accountNumber}/statement")
    public List<TransactionRecord> statement(@PathVariable String accountNumber) {
        return transactionRepository.findByAccountNumberOrderByCreatedAtDesc(accountNumber);
    }

    private void validateInrTransferOnly(String fromAccountNumber, String toAccountNumber) {
        String base = accountServiceBaseUrl.replaceAll("/+$", "");
        AccountSnapshot fromAcc =
                restTemplate.getForObject(base + "/accounts/number/" + fromAccountNumber, AccountSnapshot.class);
        AccountSnapshot toAcc =
                restTemplate.getForObject(base + "/accounts/number/" + toAccountNumber, AccountSnapshot.class);
        if (fromAcc == null || toAcc == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not resolve account for currency check");
        }
        if (!"INR".equalsIgnoreCase(fromAcc.getCurrency()) || !"INR".equalsIgnoreCase(toAcc.getCurrency())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transfers are only supported between INR accounts (no FX flow configured)");
        }
    }

    private void enforceDailyLimit(TransferRequest request) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        OffsetDateTime from = today.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime to = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        BigDecimal total = transactionRepository.dailyDebitSum(request.fromAccountNumber(), from, to);
        if (total.add(request.amount()).compareTo(DAILY_LIMIT) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Daily transfer limit exceeded for account");
        }
    }

    private ResponseEntity<String> timedAccountCall(String accountNumber, String pathWithQuery, HttpMethod method) {
        String base = accountServiceBaseUrl.replaceAll("/+$", "");
        String url = base + "/accounts/internal/" + accountNumber + pathWithQuery;
        return balanceCheckLatency.record(
                () -> withRetry(() -> restTemplate.exchange(url, method, HttpEntity.EMPTY, String.class)));
    }

    private ResponseEntity<String> withRetry(SupplierWithException<ResponseEntity<String>> supplier) {
        long waitMs = 200;
        RuntimeException latest = null;
        for (int i = 0; i < 3; i++) {
            try {
                return supplier.get();
            } catch (RuntimeException ex) {
                latest = ex;
                try { Thread.sleep(waitMs); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
                waitMs *= 2;
            }
        }
        throw latest;
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            return payload.toString();
        }
    }

    @FunctionalInterface
    interface SupplierWithException<T> { T get(); }

    public record TransferRequest(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String reference) {}
}
