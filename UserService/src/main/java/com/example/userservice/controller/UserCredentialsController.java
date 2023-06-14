package com.example.userservice.controller;

import com.example.userservice.dto.UserCredentialsRequestDto;
import com.example.userservice.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserCredentialsController {
    private final UserCredentialsService userService;

    @PostMapping("/add_new_user")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody UserCredentialsRequestDto userRequestDto, BindingResult bindingResult){

        if (bindingResult.hasFieldErrors("email")) {
            FieldError emailError = bindingResult.getFieldError("email");
            return new ResponseEntity<>(emailError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        else if (bindingResult.hasFieldErrors("phoneNumber")) {
            FieldError phoneNumberError = bindingResult.getFieldError("phoneNumber");
            return new ResponseEntity<>(phoneNumberError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        userService.addNewUser(userRequestDto);
        return new ResponseEntity<>("New User Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add_new_admin")
    public ResponseEntity<String> addNewAdmin(@RequestBody UserCredentialsRequestDto userRequestDto){
        userService.addNewAdmin(userRequestDto);
        return new ResponseEntity<>("New Admin Created Successfully", HttpStatus.CREATED);
    }
}
