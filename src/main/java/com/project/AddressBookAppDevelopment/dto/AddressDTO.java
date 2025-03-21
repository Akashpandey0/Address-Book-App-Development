package com.project.AddressBookAppDevelopment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
    @Pattern(regexp = "^[A-Z][a-zA-Z\\\\s]*$",message="Name not starting with Capital Letter")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @Pattern(regexp = "^[a-zA-Z0-9_+-]+@[a-z]+\\.[a-zA-Z0-9.-]{2,}$")
    @Email(message = "Email should be valid")
    private String email;

    public AddressDTO(String name, String address, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

}