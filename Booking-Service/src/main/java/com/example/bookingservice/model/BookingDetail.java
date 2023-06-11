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
    private int numberOfGuests;

    @ElementCollection
    private List<String> specialRequest;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    private double totalPrice;
}
