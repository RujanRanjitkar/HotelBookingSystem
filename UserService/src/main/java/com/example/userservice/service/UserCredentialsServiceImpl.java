package com.example.userservice.service;

import com.example.userservice.customException.PasswordNotMatchException;
import com.example.userservice.customException.UserAlreadyExistsException;
import com.example.userservice.dto.UserCredentialsRequestDto;
import com.example.userservice.enums.Role;
import com.example.userservice.externalservice.EmailService;
import com.example.userservice.externalservice.InventoryService;
import com.example.userservice.model.EmailSender;
import com.example.userservice.model.UserCredentials;
import com.example.userservice.repo.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final UserCredentialsRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final InventoryService inventoryService;
    private final EmailService emailService;

    private UserCredentials userDetails(UserCredentialsRequestDto userRequestDto) {

        Optional<UserCredentials> userCredentials=userRepo.findByEmail(userRequestDto.getEmail());

        if(userCredentials.isPresent()){
            throw new UserAlreadyExistsException("User with " + userRequestDto.getEmail() + " already exists");
        }

        UserCredentials user = new UserCredentials();

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setAddress(userRequestDto.getAddress());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRePassword(passwordEncoder.encode(userRequestDto.getRePassword()));

        if (!userRequestDto.getPassword().equals(userRequestDto.getRePassword())) {
            throw new PasswordNotMatchException("Password did not match");
        }

        return user;
    }

    public void sendMail(String userEmail){
        EmailSender emailSender=new EmailSender();
        emailSender.setToEmail(userEmail);
        emailSender.setBody("You can now access Hotel Booking System");
        emailSender.setSubject("Welcome to Hotel Booking System");
        emailService.sendEmail(emailSender);
    }

    @Override
    public void addNewUser(UserCredentialsRequestDto userRequestDto) {
        UserCredentials user = userDetails(userRequestDto);

        String ownerEmail = inventoryService.getHotelByHotelOwnerEmail(userRequestDto.getEmail());

        if (ownerEmail.equals(userRequestDto.getEmail())) {
            user.setRole(Role.HOTEL_OWNER);
        } else {
            user.setRole(Role.USER);
        }

        sendMail(userRequestDto.getEmail());

        userRepo.save(user);

    }

    @Override
    public void addNewAdmin(UserCredentialsRequestDto userRequestDto) {
        UserCredentials user = userDetails(userRequestDto);
        user.setRole(Role.ADMIN);

        sendMail(userRequestDto.getEmail());

        userRepo.save(user);
    }
}
