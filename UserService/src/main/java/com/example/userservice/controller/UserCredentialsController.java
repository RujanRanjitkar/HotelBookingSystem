package com.example.userservice.controller;

import com.example.userservice.dto.UserCredentialsRequestDto;
import com.example.userservice.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserCredentialsController {
    private final UserCredentialsService userService;

    @PostMapping("/add_new_user")
    public ResponseEntity<String> addNewUser(@RequestBody UserCredentialsRequestDto userRequestDto){
        userService.addNewUser(userRequestDto);
        return new ResponseEntity<>("New User Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add_new_admin")
    public ResponseEntity<String> addNewAdmin(@RequestBody UserCredentialsRequestDto userRequestDto){
        userService.addNewAdmin(userRequestDto);
        return new ResponseEntity<>("New Admin Created Successfully", HttpStatus.CREATED);
    }
}
