package com.example.emailservice.service;

import com.example.emailservice.model.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailSender emailSender) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom("rujanranjit122@gmail.com");
        messageHelper.setTo(emailSender.getToEmail());
        messageHelper.setSubject(emailSender.getSubject());
        messageHelper.setText(emailSender.getBody());
        javaMailSender.send(message);
    }
}
