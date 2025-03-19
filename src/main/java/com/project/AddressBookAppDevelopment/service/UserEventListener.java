package com.project.AddressBookAppDevelopment.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @RabbitListener(queues = "user.queue")
    public void receiveUserEvent(String message) {
        System.out.println("Received User Event: " + message);
        // Send an email notification here
    }
}
