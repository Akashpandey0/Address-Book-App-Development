package com.project.AddressBookAppDevelopment.controller;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import com.project.AddressBookAppDevelopment.model.Address;
import com.project.AddressBookAppDevelopment.rabbitmq.AddressBookProducer;
import com.project.AddressBookAppDevelopment.service.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/addressbook")
public class AddressController {

    @Autowired
    private IAddressService addressBookService;

    @Autowired
    private AddressBookProducer addressBookProducer;

    @PostMapping("/add")
    @Operation(summary = "Adds a new contact", description = "Adds a new contact")
    public ResponseEntity<Address> addContact(@Valid @RequestBody AddressDTO addressBookDTO) {
        Address addedContact = addressBookService.addContact(addressBookDTO);
        String message = "New contact added: " + addedContact.getName() + " (" + addedContact.getEmail() + ")";
        addressBookProducer.sendMessage(message);
        return ResponseEntity.ok(addedContact);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets contact by id", description = "Searches a contact by id")
    public ResponseEntity<Address> getContactById(@PathVariable int id) {
        Address contact = addressBookService.getContactById(id);
        String message = "Contact retrieved: " + contact.getName() + " (" + contact.getEmail() + ")";
        addressBookProducer.sendMessage(message);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/all")
    @Operation(summary = "Gets all contacts", description = "Gives all contacts")
    public ResponseEntity<List<Address>> getAllContacts() {
        List<Address> contacts = addressBookService.getAllContacts();
        String message = contacts.size() + " contacts retrieved.";
        addressBookProducer.sendMessage(message);
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a contact", description = "Update a contact")
    public ResponseEntity<Address> updateContact(@PathVariable int id, @Valid @RequestBody AddressDTO addressBookDTO) {
        Address updatedContact = addressBookService.updateContact(id, addressBookDTO);
        String message = "Contact updated: " + updatedContact.getName() + " (" + updatedContact.getEmail() + ")";
        addressBookProducer.sendMessage(message);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a contact", description = "Deletes a contact")
    public ResponseEntity<Void> deleteContact(@PathVariable int id) {
        addressBookService.deleteContact(id);
        String message = "Contact with ID " + id + " deleted.";
        addressBookProducer.sendMessage(message);
        return ResponseEntity.noContent().build();
    }
}