package com.example.bookingservice.customException;

public class RoomNotAvailableException extends RuntimeException{
    public RoomNotAvailableException(String message) {
        super(message);
    }
}
