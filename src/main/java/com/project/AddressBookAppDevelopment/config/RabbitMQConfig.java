package com.project.AddressBookAppDevelopment.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userQueue() {
        return new Queue("user.queue", true, false, false); // Durable, Non-Exclusive, Non-AutoDelete
    }

    @Bean
    public Queue addressQueue() {
        return new Queue("address.queue", true, false, false); // Durable, Non-Exclusive, Non-AutoDelete
    }

    @Bean
    public Queue passwordQueue() {
        return new Queue("password.queue", true, false, false); // Durable, Non-Exclusive, Non-AutoDelete
    }
}
