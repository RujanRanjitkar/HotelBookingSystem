package com.example.userservice.loginForm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginFormRequestDto {
    private String email;
    private String password;
}
