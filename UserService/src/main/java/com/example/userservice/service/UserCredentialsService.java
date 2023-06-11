package com.example.userservice.service;

import com.example.userservice.dto.UserCredentialsRequestDto;

public interface UserCredentialsService {
    void addNewUser(UserCredentialsRequestDto userRequestDto);
    void addNewAdmin(UserCredentialsRequestDto userRequestDto);
}
