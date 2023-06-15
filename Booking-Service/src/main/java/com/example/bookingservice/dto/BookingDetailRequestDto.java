package com.example.bookingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookingDetailRequestDto {
    @NotNull(message = "Please provide check-in-date")
    private LocalDate checkInDate;
    @NotNull(message = "Please provide check-out-date")
    private LocalDate checkOutDate;
    private List<String> specialRequest;
    @NotBlank(message = "You must choice payment option")
    private String paymentOption;
    private Long roomId;
    private String bookedBy;
}
