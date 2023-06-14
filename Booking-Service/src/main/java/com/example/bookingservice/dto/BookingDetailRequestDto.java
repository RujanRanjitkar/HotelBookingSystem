package com.example.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookingDetailRequestDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private List<String> specialRequest;
    private String paymentOption;
    private Long roomId;
    private String hotelName;
    private String bookedBy;
}
