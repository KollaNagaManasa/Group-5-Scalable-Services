package com.customer.service;

import com.customer.dto.CustomerEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(CustomerEventProducer.class);
    private static final String TOPIC = "customer-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishEvent(CustomerEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, String.valueOf(event.getCustomerId()), message);
            logger.info("Published {} to topic {}: {}", event.getEventType(), TOPIC, message);
        } catch (Exception e) {
            logger.error("Failed to publish event to Kafka: {}", e.getMessage(), e);
        }
    }
}
