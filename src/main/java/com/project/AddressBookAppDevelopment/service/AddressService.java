package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import com.project.AddressBookAppDevelopment.model.Address;
import com.project.AddressBookAppDevelopment.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    //Autowired imported and added.

    @Autowired
    private AddressRepository addressRepository;

    // Create a new address
    public Address addAddress(AddressDTO addressDTO) {
        Address address = new Address(
                addressDTO.getName(),
                addressDTO.getEmail(),
                addressDTO.getPhone(),
                addressDTO.getCity()
        );
        return addressRepository.save(address);
    }

    // Retrieve all addresses
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    // Get Address by ID
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    // Update Address
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        Optional<Address> existingAddress = addressRepository.findById(id);
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setName(addressDTO.getName());
            address.setEmail(addressDTO.getEmail());
            address.setPhone(addressDTO.getPhone());
            address.setCity(addressDTO.getCity());
            return addressRepository.save(address);
        }
        return null;
    }

    // Delete Address
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
