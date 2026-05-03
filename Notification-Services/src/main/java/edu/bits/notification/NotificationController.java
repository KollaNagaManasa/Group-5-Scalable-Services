package edu.bits.notification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {}

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("50000");
    private final NotificationLogRepository repository;

    public NotificationController(NotificationLogRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "txn.notification.queue")
    public void onTransactionCreated(Map<String, Object> payload) {
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        if (amount.compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            NotificationLog log = new NotificationLog();
            log.setType("HIGH_VALUE_TXN");
            log.setDestination("SMS/EMAIL");
            log.setMessage("High value txn alert: " + payload);
            repository.save(log);
        }
    }

    @PostMapping("/account-status")
    public NotificationLog notifyAccountStatus(@RequestBody Map<String, String> body) {
        NotificationLog log = new NotificationLog();
        log.setType("ACCOUNT_STATUS");
        log.setDestination("SMS/EMAIL");
        log.setMessage("Account status updated: " + body);
        return repository.save(log);
    }

    @GetMapping
    public List<NotificationLog> all() {
        return repository.findAll();
    }
}
