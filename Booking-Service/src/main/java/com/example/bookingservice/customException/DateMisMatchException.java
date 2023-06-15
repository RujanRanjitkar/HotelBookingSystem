package com.example.bookingservice.customException;

public class DateMisMatchException extends RuntimeException{
    public DateMisMatchException(String message) {
        super(message);
    }
}
