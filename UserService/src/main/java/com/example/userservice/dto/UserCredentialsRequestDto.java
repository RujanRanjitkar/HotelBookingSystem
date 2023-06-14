package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserCredentialsRequestDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number format. It should be a 10-digit number.")
    private String phoneNumber;

    @Pattern(regexp = "[a-zA-Z0-9]+@gmail\\.com", message = "Invalid email format")
    private String email;

    private String password;
    private String rePassword;
}
