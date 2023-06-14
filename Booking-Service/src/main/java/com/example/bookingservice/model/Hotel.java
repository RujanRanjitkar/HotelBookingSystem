package com.example.bookingservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Hotel {
    private Long hotelId;
    private String hotelName;
    private Address address;
    private String description;
    private String createdBy;
    private List<HotelImage> hotelImages;
    private List<Contact> contacts;

    private List<Room> rooms;
}
