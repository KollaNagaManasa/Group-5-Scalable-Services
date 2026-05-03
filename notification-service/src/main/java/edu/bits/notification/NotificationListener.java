package edu.bits.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @RabbitListener(queues = "txn.notification.queue")
    public void handleMessage(String message) {
        System.out.println("🔥 Received message: " + message);
    }
}
