package com.example.userservice.loginForm.controller;

import com.example.userservice.loginForm.dto.LoginFormRequestDto;
import com.example.userservice.loginForm.dto.LoginFormResponseDto;
import com.example.userservice.loginForm.service.LoginFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class LoginFormController {
    private final LoginFormService loginFormService;

    @PostMapping("/authenticate")
    public LoginFormResponseDto authenticate(@RequestBody LoginFormRequestDto loginFormRequestDto) {
        return loginFormService.authenticate(loginFormRequestDto);
    }
}
