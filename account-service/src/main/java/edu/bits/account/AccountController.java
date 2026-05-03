package edu.bits.account;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepository repository;
    private final RestTemplate customerRestTemplate;

    @Value("${CUSTOMER_SERVICE_BASE_URL:http://localhost:8081}")
    private String customerServiceBaseUrl;

    public AccountController(AccountRepository repository, RestTemplate customerRestTemplate) {
        this.repository = repository;
        this.customerRestTemplate = customerRestTemplate;
    }

    @GetMapping
    public List<Account> all() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Account byId(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @GetMapping("/number/{accountNumber}")
    public Account byNumber(@PathVariable String accountNumber) {
        return repository.findByAccountNumber(accountNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        replicateCustomerNameIfMissing(account);
        return repository.save(account);
    }

    private void replicateCustomerNameIfMissing(Account account) {
        if (account.getCustomerId() == null) {
            return;
        }
        if (account.getCustomerName() != null && !account.getCustomerName().isBlank()) {
            return;
        }
        try {
            String base = customerServiceBaseUrl.replaceAll("/+$", "");
            CustomerSnapshot customer = customerRestTemplate.getForObject(
                    base + "/customers/" + account.getCustomerId(), CustomerSnapshot.class);
            if (customer != null && customer.getName() != null) {
                account.setCustomerName(customer.getName());
            }
        } catch (RuntimeException ignored) {
            // Client may supply customerName explicitly if Customer Service is unavailable.
        }
    }

    @PatchMapping("/{id}/status")
    public Account updateStatus(@PathVariable Long id, @RequestParam String status) {
        Account account = byId(id);
        account.setStatus(status);
        return repository.save(account);
    }

    @PostMapping("/internal/{accountNumber}/debit")
    @Transactional
    public Account debit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        Account account = byNumber(accountNumber);
        validateTransactable(account);
        if (isNoOverdraftAccountType(account.getAccountType()) && account.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds: overdraft not allowed");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return repository.save(account);
    }

    @PostMapping("/internal/{accountNumber}/credit")
    @Transactional
    public Account credit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        Account account = byNumber(accountNumber);
        validateTransactable(account);
        account.setBalance(account.getBalance().add(amount));
        return repository.save(account);
    }

    private static boolean isNoOverdraftAccountType(String accountType) {
        if (accountType == null) {
            return false;
        }
        String t = accountType.trim();
        return "SAVINGS".equalsIgnoreCase(t) || "SALARY".equalsIgnoreCase(t);
    }

    private void validateTransactable(Account account) {
        if ("FROZEN".equalsIgnoreCase(account.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transactions not allowed on frozen account");
        }
        if ("CLOSED".equalsIgnoreCase(account.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transactions not allowed on closed account");
        }
    }
}
