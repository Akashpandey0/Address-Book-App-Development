package com.project.AddressBookAppDevelopment.model;

import com.project.AddressBookAppDevelopment.dto.AddressDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "addressbook")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String address;
    private String phoneNumber;
    private String email;

    public Address(AddressDTO dto) {
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.phoneNumber = dto.getPhoneNumber();
        this.email = dto.getEmail();
    }

}