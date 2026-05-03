package edu.bits.notification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("txn.notification.queue", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("txn.exchange");
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("txn.created");
    }
}
