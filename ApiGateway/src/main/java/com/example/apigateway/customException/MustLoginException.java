package com.example.apigateway.customException;

public class MustLoginException extends RuntimeException{
    public MustLoginException(String message) {
        super(message);
    }
}
