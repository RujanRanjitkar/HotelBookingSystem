package com.example.inventoryservice.hotel.dto;

import com.example.inventoryservice.hotel.model.Address;
import com.example.inventoryservice.hotel.model.Contact;
import com.example.inventoryservice.room.dto.RoomRequestDto;
import com.example.inventoryservice.room.model.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelRequestDto {
    private String hotelName;
    private Address address;
    private String description;
    private String createdBy;
    private List<Contact> contacts;
    private String hotelOwnerEmail;
}
