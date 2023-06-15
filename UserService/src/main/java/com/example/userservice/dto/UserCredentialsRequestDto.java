package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserCredentialsRequestDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number format. It should be a 10-digit number")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
    private String rePassword;
}
