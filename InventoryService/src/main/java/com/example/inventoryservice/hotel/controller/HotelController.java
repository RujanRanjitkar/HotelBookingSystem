package com.example.inventoryservice.hotel.controller;

import com.example.inventoryservice.hotel.dto.HotelRequestDto;
import com.example.inventoryservice.hotel.dto.HotelResponseDto;
import com.example.inventoryservice.hotel.service.HotelService;
import com.example.inventoryservice.hotel.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final JwtUtil jwtUtil;

    @Value(("${project.image}"))
    private String path;

    @PostMapping(value = "/add_new_hotel")
    public ResponseEntity<String> addHotel(@RequestPart HotelRequestDto hotelRequestDto,
                                           @RequestPart List<MultipartFile> hotelImages,
                                           @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        String token=authorizationHeader.substring(7); //Remove "Bearer " from token
        String userName= jwtUtil.extractUserName(token);
        hotelRequestDto.setCreatedBy(userName);

        hotelService.addNewHotel(hotelRequestDto, path, hotelImages);
        return new ResponseEntity<>("New Hotel Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get_all_hotels")
    public ResponseEntity<List<HotelResponseDto>> getAllHotels() throws IOException {

        List<HotelResponseDto> hotelResponseDto = hotelService.getAllHotels();
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }

    @GetMapping("/get_hotel_by_hotelId/{hotelId}")
    public ResponseEntity<HotelResponseDto> getHotelByHotelId(@PathVariable Long hotelId) throws IOException {

        HotelResponseDto hotel= hotelService.getHotelByHotelId(hotelId);
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }

    @GetMapping("/get_hotels_by_location/{location}")
    public ResponseEntity<List<HotelResponseDto>> getAllHotelsByLocation(@PathVariable String location) throws IOException {

        List<HotelResponseDto> hotelResponseDto = hotelService.getHotelsByLocation(location);
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }
}
