package com.example.inventoryservice.hotel.service;

import com.example.inventoryservice.hotel.dto.HotelRequestDto;
import com.example.inventoryservice.hotel.dto.HotelResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HotelService {

    void addNewHotel(HotelRequestDto hotelRequestDto, String path, List<MultipartFile> hotelImages) throws IOException;
    List<HotelResponseDto> getAllHotels() throws IOException;
    HotelResponseDto getHotelByHotelId(Long hotelId) throws IOException;

    List<HotelResponseDto> getHotelsByLocation(String location);
}
