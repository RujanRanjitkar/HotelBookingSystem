package com.example.inventoryservice.room.dto;

import com.example.inventoryservice.hotel.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelRoomRequestDto{
    private String hotelName;
    private Address address;
}
