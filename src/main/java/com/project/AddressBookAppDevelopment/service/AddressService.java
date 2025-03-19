package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.model.Address;
import com.project.AddressBookAppDevelopment.repository.AddressRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // ✅ GET All Addresses (Caches in Redis)
    @Cacheable(value = "addresses")
    public List<Address> getAllAddresses() {
        System.out.println("Fetching addresses from DB...");
        return addressRepository.findAll();
    }

    // ✅ GET Address by ID (Caches in Redis)
    @Cacheable(value = "address", key = "#id")
    public Optional<Address> getAddressById(Long id) {
        System.out.println("Fetching Address ID " + id + " from DB...");
        return addressRepository.findById(id);
    }

    // ✅ CREATE Address (Clears Cache & Publishes Event)
    @CacheEvict(value = {"addresses"}, allEntries = true)  // Clears list cache
    public Address addAddress(Address address) {
        Address savedAddress = addressRepository.save(address);

        // 🔔 Publish event to RabbitMQ
        rabbitTemplate.convertAndSend("address.queue", "New address added: " + savedAddress.getName());

        return savedAddress;
    }

    // ✅ UPDATE Address (Updates Cache & Publishes Event)
    @CachePut(value = "address", key = "#id")
    public Address updateAddress(Long id, Address addressDetails) {
        Optional<Address> existingAddress = addressRepository.findById(id);

        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setName(addressDetails.getName());
            address.setEmail(addressDetails.getEmail());
            address.setPhone(addressDetails.getPhone());
            address.setCity(addressDetails.getCity());

            Address updatedAddress = addressRepository.save(address);

            // 🔔 Publish event to RabbitMQ
            rabbitTemplate.convertAndSend("address.queue", "Address updated: " + updatedAddress.getName());

            return updatedAddress;
        }
        return null;
    }

    // ✅ DELETE Address (Clears Cache & Publishes Event)
    @CacheEvict(value = {"addresses", "address"}, allEntries = true)
    public boolean deleteAddress(Long id) {
        Optional<Address> existingAddress = addressRepository.findById(id);

        if (existingAddress.isPresent()) {
            addressRepository.deleteById(id);

            // 🔔 Publish event to RabbitMQ
            rabbitTemplate.convertAndSend("address.queue", "Address deleted: " + id);

            return true;
        }
        return false;
    }
}
