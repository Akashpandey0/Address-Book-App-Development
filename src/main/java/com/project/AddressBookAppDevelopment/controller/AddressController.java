package com.project.AddressBookAppDevelopment.controller;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
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

    // CREATE - Add an Address
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.addAddress(addressDTO));
    }

    // READ - Get All Addresses
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    // READ - Get Address by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<AddressDTO>> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    // UPDATE - Update an Address
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, addressDTO);
        return updatedAddress != null ? ResponseEntity.ok(updatedAddress) : ResponseEntity.notFound().build();
    }

    // DELETE - Remove an Address
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        boolean isDeleted = addressService.deleteAddress(id);
        return isDeleted ? ResponseEntity.ok("Address deleted successfully") : ResponseEntity.notFound().build();
    }
}
