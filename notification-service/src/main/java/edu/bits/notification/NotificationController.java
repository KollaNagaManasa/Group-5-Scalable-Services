package edu.bits.notification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
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
