package com.example.bookingservice.model;

import com.example.bookingservice.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @ElementCollection
    private List<String> specialRequest;

    private String paymentOption;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private Long roomId;
    private String bookedBy;
}
