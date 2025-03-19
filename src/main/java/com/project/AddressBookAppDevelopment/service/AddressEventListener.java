package com.project.AddressBookAppDevelopment.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AddressEventListener {

    @RabbitListener(queues = "address.queue")
    public void receiveAddressEvent(String message) {
        System.out.println("Received Address Event: " + message);
        // Trigger additional actions if needed
    }
}
