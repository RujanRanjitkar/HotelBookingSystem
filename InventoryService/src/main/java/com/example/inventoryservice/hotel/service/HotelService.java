package com.example.inventoryservice.hotel.service;

import com.example.inventoryservice.hotel.dto.HotelRequestDto;
import com.example.inventoryservice.hotel.dto.HotelResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HotelService {

    void addNewHotel(HotelRequestDto hotelRequestDto, String path, List<MultipartFile> hotelImages) throws IOException;
    List<HotelResponseDto> getAllHotels();
    HotelResponseDto getHotelByHotelId(Long hotelId);

    List<HotelResponseDto> getHotelsByLocation(String location);

    List<HotelResponseDto> getHotels(String hotelName, String location, String city, Double price, Pageable pageable);

    String getHotelByHotelOwnerEmail(String email);

    void updateHotelInfo(Long hotelId, HotelRequestDto hotelRequestDto, String path, List<MultipartFile> hotelImages, String authorizationHeader) throws IOException;
}
