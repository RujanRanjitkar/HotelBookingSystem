package com.example.emailservice.controller;

import com.example.emailservice.model.EmailSender;
import com.example.emailservice.service.EmailSenderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailSenderController {
    private final EmailSenderService emailSenderService;

    @PostMapping("/email")
    public void sendEmail(@RequestBody EmailSender emailSender) throws MessagingException {
        emailSenderService.sendEmail(emailSender);
    }
}
