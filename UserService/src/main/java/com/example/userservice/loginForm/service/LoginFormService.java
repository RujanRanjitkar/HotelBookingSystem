package com.example.userservice.loginForm.service;

import com.example.userservice.config.JwtService;
import com.example.userservice.loginForm.dto.LoginFormRequestDto;
import com.example.userservice.loginForm.dto.LoginFormResponseDto;
import com.example.userservice.repo.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginFormService {
    private final UserCredentialsRepo userCredentialsRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginFormResponseDto authenticate(LoginFormRequestDto loginFormRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginFormRequestDto.getEmail(), loginFormRequestDto.getPassword()
                )
        );

        var user = userCredentialsRepo.findByEmail(loginFormRequestDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        var jwtToken = jwtService.generateToken(user);

        return LoginFormResponseDto.builder()
                .token(jwtToken)
                .build();
    }
}
