package com.example.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private String paymentOption;
    private double amount;
}
