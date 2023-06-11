package com.example.inventoryservice.room.dto;

import com.example.inventoryservice.hotel.dto.ImageResponseDto;
import com.example.inventoryservice.room.enums.RoomStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomResponseDto {
    private String roomNumber;
    private String roomType;
    private RoomStatus roomStatus;
    private List<ImageResponseDto> roomImages;
    private double price;
}
