package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import com.project.AddressBookAppDevelopment.model.Address;

import java.util.List;

public interface IAddressService {
    Address addContact(AddressDTO addressBookDTO);
    Address updateContact(int id, AddressDTO addressBookDTO);
    Address getContactById(int id);
    List<Address> getAllContacts();
    void deleteContact(int id);
}