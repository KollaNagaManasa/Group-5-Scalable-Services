package edu.bits.notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    DirectExchange txnExchange() {
        return new DirectExchange("txn.exchange", true, false);
    }

    @Bean
    Queue txnQueue() {
        return new Queue("txn.notification.queue", true);
    }

    @Bean
    Binding txnBinding(Queue txnQueue, DirectExchange txnExchange) {
        return BindingBuilder.bind(txnQueue).to(txnExchange).with("txn.created");
    }
}
