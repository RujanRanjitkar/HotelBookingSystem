package com.example.inventoryservice.room.dto;

import com.example.inventoryservice.hotel.dto.ImageResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomRequestDto {
    private String roomNumber;
    private String roomType;
    private double price;
    private HotelRoomRequestDto hotel;
}
