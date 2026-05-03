package edu.bits.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationListener {

    @RabbitListener(queues = "txn.notification.queue")
    public void handleMessage(Map<String, Object> message) {

        System.out.println(" Notification received!");

        System.out.println("Event Type: " + message.get("eventType"));
        System.out.println("From Account: " + message.get("fromAccount"));
        System.out.println("To Account: " + message.get("toAccount"));
        System.out.println("Amount: " + message.get("amount"));
        System.out.println("Reference: " + message.get("reference"));
        System.out.println("Correlation ID: " + message.get("correlationId"));

        System.out.println("Full Payload: " + message);
    }
}
