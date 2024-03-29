package com.example.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {
    private double amount;
    private String transaction_code;
    private String unique_id;
    private String status;
}
