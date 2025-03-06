package com.project.AddressBookAppDevelopment.controller;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import com.project.AddressBookAppDevelopment.model.Address;
import com.project.AddressBookAppDevelopment.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

//    Set up application.properties for MySQL Configuration
//    Create a RestController to handle HTTP requests
//    Use ResponseEntity to return JSON responses

    // Create Address
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressDTO addressDTO) {
        Address savedAddress = addressService.addAddress(addressDTO);
        return ResponseEntity.ok(savedAddress);
    }

    // Get All Addresses
    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    // Get Address by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Address>> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    // Update Address
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        Address updatedAddress = addressService.updateAddress(id, addressDTO);
        if (updatedAddress != null) {
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete Address
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
