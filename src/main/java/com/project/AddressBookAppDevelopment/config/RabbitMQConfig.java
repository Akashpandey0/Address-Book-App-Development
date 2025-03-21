package com.project.AddressBookAppDevelopment.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue addressBookQueue() {
        return new Queue("addressbook.queue", true); // Declare a queue
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("addressbook.exchange"); // Declare an exchange
    }

    @Bean
    public Binding binding(Queue addressBookQueue, TopicExchange exchange) {
        return BindingBuilder.bind(addressBookQueue)
                .to(exchange)
                .with("addressbook.routingKey"); // Bind the queue to the exchange
    }
}