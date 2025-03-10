package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final List<AddressDTO> addressList = new ArrayList<>();
    private long idCounter = 1; // To assign unique IDs

    // CREATE - Add an Address
    public AddressDTO addAddress(AddressDTO addressDTO) {
        addressDTO.setId(idCounter++); // Assign a unique ID
        addressList.add(addressDTO);
        return addressDTO;
    }

    // READ - Get All Addresses
    public List<AddressDTO> getAllAddresses() {
        return addressList;
    }

    // READ - Get Address by ID
    public Optional<AddressDTO> getAddressById(Long id) {
        return addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst();
    }

    // UPDATE - Update an Address
    public AddressDTO updateAddress(Long id, AddressDTO updatedAddress) {
        Optional<AddressDTO> existingAddress = getAddressById(id);
        if (existingAddress.isPresent()) {
            AddressDTO address = existingAddress.get();
            address.setName(updatedAddress.getName());
            address.setEmail(updatedAddress.getEmail());
            address.setPhone(updatedAddress.getPhone());
            address.setCity(updatedAddress.getCity());
            return address;
        }
        return null;
    }

    // DELETE - Remove an Address
    public boolean deleteAddress(Long id) {
        return addressList.removeIf(address -> address.getId().equals(id));
    }
}
