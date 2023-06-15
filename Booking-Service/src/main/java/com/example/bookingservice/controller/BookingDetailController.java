package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingDetailRequestDto;
import com.example.bookingservice.service.BookingDetailService;
import com.example.bookingservice.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/booking_details")
@RequiredArgsConstructor
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;
    private final JwtUtil jwtUtil;

    @PostMapping("book_rooms")
    public ResponseEntity<String> bookRooms(@Valid @RequestBody BookingDetailRequestDto bookingDetailRequestDto, @RequestHeader("Authorization") String authorizationHeader){

        String token=authorizationHeader.substring(7); //Remove "Bearer " from token
        String userEmail= jwtUtil.extractUserName(token);
        bookingDetailRequestDto.setBookedBy(userEmail);

        bookingDetailService.bookRooms(bookingDetailRequestDto);
        return new ResponseEntity<>("Rooms has been booked", HttpStatus.CREATED);
    }
}
