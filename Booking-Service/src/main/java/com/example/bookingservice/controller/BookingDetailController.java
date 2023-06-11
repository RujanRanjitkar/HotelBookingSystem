package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingDetailRequestDto;
import com.example.bookingservice.service.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/booking_details")
@RequiredArgsConstructor
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;

    @PostMapping("book_rooms")
    public ResponseEntity<String> bookRooms(@RequestBody BookingDetailRequestDto bookingDetailRequestDto){

        bookingDetailService.bookRooms(bookingDetailRequestDto);
        return new ResponseEntity<>("Rooms has been booked", HttpStatus.CREATED);
    }
}
