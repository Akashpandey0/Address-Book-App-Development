package com.project.AddressBookAppDevelopment.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  // Add this annotation to make it a Spring Bean
public class AddressBookProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String message) {
        amqpTemplate.convertAndSend("addressbook.exchange", "addressbook.routingKey", message);
        System.out.println("Message sent to RabbitMQ: " + message);
    }
}
