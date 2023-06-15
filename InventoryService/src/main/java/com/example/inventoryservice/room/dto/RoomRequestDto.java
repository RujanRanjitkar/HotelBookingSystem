package com.example.inventoryservice.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequestDto {
    @NotBlank(message = "Please specify room number")
    private String roomNumber;
    @NotBlank(message = "Please specify room type")
    private String roomType;
    @NotNull(message = "Please specify price")
    private Double price;
    private HotelRoomRequestDto hotel;
}
