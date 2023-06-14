package com.example.inventoryservice.hotel.controller;

import com.example.inventoryservice.hotel.dto.HotelRequestDto;
import com.example.inventoryservice.hotel.dto.HotelResponseDto;
import com.example.inventoryservice.hotel.service.HotelService;
import com.example.inventoryservice.hotel.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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

        String token = authorizationHeader.substring(7); //Remove "Bearer " from token
        String userName = jwtUtil.extractUserName(token);
        hotelRequestDto.setCreatedBy(userName);

        hotelService.addNewHotel(hotelRequestDto, path, hotelImages);
        return new ResponseEntity<>("New Hotel Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get_all_hotels")
    public ResponseEntity<List<HotelResponseDto>> getAllHotels() {

        List<HotelResponseDto> hotelResponseDto = hotelService.getAllHotels();
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }

    @GetMapping("/get_hotel_by_hotelId/{hotelId}")
    public ResponseEntity<HotelResponseDto> getHotelByHotelId(@PathVariable Long hotelId) {

        HotelResponseDto hotel = hotelService.getHotelByHotelId(hotelId);
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }

    /*@GetMapping("/get_hotel_by_hotel_owner_email/{email}")
    public ResponseEntity<HotelResponseDto> getHotelByHotelOwnerEmail(@PathVariable String email) {

        HotelResponseDto hotel = hotelService.getHotelByHotelOwnerEmail(email);
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }*/
    @GetMapping("/get_hotel_by_hotel_owner_email/{email}")
    public ResponseEntity<String> getHotelByHotelOwnerEmail(@PathVariable String email) {

        String data = hotelService.getHotelByHotelOwnerEmail(email);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/get_hotels_by_location/{location}")
    public ResponseEntity<List<HotelResponseDto>> getAllHotelsByLocation(@PathVariable String location) {

        List<HotelResponseDto> hotelResponseDto = hotelService.getHotelsByLocation(location);
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelResponseDto>> getHotels(@RequestParam(required = false) String hotelName,
                                                            @RequestParam(required = false) String location,
                                                            @RequestParam(required = false) String city,
                                                            @RequestParam(required = false) Double price,
                                                            Pageable pageable) {
        List<HotelResponseDto> hotelResponseDto = hotelService.getHotels(hotelName, location, city, price, pageable);
        return ResponseEntity.ok(hotelResponseDto);
    }

    @PutMapping(value = "/update_hotelInfo/{hotelId}")
    public ResponseEntity<String> updateHotelInfo(@PathVariable Long hotelId, @RequestPart HotelRequestDto hotelRequestDto,
                                           @RequestPart List<MultipartFile> hotelImages) throws IOException {

        hotelService.updateHotelInfo(hotelId, hotelRequestDto, path, hotelImages);
        return new ResponseEntity<>("Hotel Updated Successfully", HttpStatus.CREATED);
    }
}
