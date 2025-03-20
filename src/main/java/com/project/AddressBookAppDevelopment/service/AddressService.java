package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import com.project.AddressBookAppDevelopment.model.Address;
import com.project.AddressBookAppDevelopment.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // Convert Address Entity to DTO
    private AddressDTO convertToDTO(Address address) {
        return new AddressDTO(address.getId(), address.getName(), address.getEmail(), address.getPhone(), address.getCity());
    }

    // Convert DTO to Address Entity
    private Address convertToEntity(AddressDTO addressDTO) {
        return new Address(addressDTO.getId(), addressDTO.getName(), addressDTO.getEmail(), addressDTO.getPhone(), addressDTO.getCity());
    }

    // ✅ CREATE - Add an Address
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Address address = convertToEntity(addressDTO);
        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }

    // ✅ READ - Get All Addresses
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ READ - Get Address by ID
    public Optional<AddressDTO> getAddressById(Long id) {
        return addressRepository.findById(id).map(this::convertToDTO);
    }

    // ✅ UPDATE - Update an Address
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Optional<Address> existingAddress = addressRepository.findById(id);
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setName(addressDTO.getName());
            address.setEmail(addressDTO.getEmail());
            address.setPhone(addressDTO.getPhone());
            address.setCity(addressDTO.getCity());
            Address updatedAddress = addressRepository.save(address);
            return convertToDTO(updatedAddress);
        }
        return null;
    }

    // ✅ DELETE - Remove an Address
    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
