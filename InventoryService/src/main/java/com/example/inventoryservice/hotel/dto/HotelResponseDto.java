package com.example.inventoryservice.hotel.dto;

import com.example.inventoryservice.hotel.model.Address;
import com.example.inventoryservice.hotel.model.HotelImage;
import com.example.inventoryservice.room.dto.RoomResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponseDto {
    private String hotelName;
    private AddressResponseDto address;
    private String description;
    private List<ImageResponseDto> hotelImages;
    private List<ContactResponseDto> contacts;
    private List<RoomResponseDto> rooms;
}
