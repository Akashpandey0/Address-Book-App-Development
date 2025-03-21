package com.project.AddressBookAppDevelopment.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


public class AddressBookConsumer {
    @RabbitListener(queues = "addressbook.queue")
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
    }
}
