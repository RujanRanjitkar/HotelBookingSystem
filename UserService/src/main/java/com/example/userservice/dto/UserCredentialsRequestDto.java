package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsRequestDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private String rePassword;
}
