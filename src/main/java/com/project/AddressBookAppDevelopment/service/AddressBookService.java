package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import com.project.AddressBookAppDevelopment.exception.ContactNotFoundException;
import com.project.AddressBookAppDevelopment.model.Address;
import com.project.AddressBookAppDevelopment.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class AddressBookService implements IAddressService {

    @Autowired
    private AddressRepository repository;

    @Override
    public Address addContact(AddressDTO addressBookDTO) {
        log.info("Adding Address Entry: {}", addressBookDTO);
        Address contact = new Address(addressBookDTO);
        return repository.save(contact);
    }

    @Override
    public Address updateContact(int id, AddressDTO addressBookDTO) {
        log.info("Updating address with id: {}", id);
        Address contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found for ID: " + id));

        contact.setName(addressBookDTO.getName());
        contact.setAddress(addressBookDTO.getAddress());
        contact.setPhoneNumber(addressBookDTO.getPhoneNumber());
        contact.setEmail(addressBookDTO.getEmail());

        return repository.save(contact);
    }

    @Override
    public Address getContactById(int id) {
        log.info("Fetching address with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found for ID: " + id));
    }

    @Override
    public List<Address> getAllContacts() {
        log.info("Fetching all address entries");
        return repository.findAll();
    }

    @Override
    public void deleteContact(int id) {
        log.info("Deleting address with id: {}", id);
        Address contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found for ID: " + id));
        repository.delete(contact);
    }
}