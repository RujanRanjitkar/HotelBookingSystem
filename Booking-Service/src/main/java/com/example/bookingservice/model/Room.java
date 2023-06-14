package com.example.bookingservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Room {
    private Long roomId;
    private String roomNumber;
    private String roomType;

    private RoomStatus roomStatus;
    private double price;

    private List<RoomImage> roomImages;

    private Hotel hotel;
}
