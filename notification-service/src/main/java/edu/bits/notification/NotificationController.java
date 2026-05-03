package edu.bits.notification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.OffsetDateTime;

public interface NotificationRepository extends JpaRepository<NotificationLog, Long> {}

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("50000");
    private final NotificationRepository repository;

    public NotificationController(NotificationRepository repository) {
        this.repository = repository;
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

@Service
class NotificationListener {

    private final NotificationRepository repository;

    public NotificationListener(NotificationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "txn.notification.queue")
    public void handleMessage(Map<String, Object> message) {
        NotificationLog log = new NotificationLog();
        log.setType((String) message.get("eventType"));
        log.setMessage("Transfer of " + message.get("amount") +
                       " from " + message.get("fromAccount") +
                       " to " + message.get("toAccount"));
        log.setDestination((String) message.get("fromAccount"));
        repository.save(log);
    }
}

@Entity
@Table(name = "notifications_log")
class NotificationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String message;
    private String destination;
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() { this.createdAt = OffsetDateTime.now(); }

    // getters + setters
}
