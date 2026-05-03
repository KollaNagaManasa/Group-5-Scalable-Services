package edu.bits.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class NotificationListener {

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
        System.out.println("Notification persisted: " + log.getMessage());
    }
}