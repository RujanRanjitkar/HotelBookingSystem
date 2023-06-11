package com.example.apigateway.customException;

public class RoleNotMatchException extends RuntimeException{
    public RoleNotMatchException(String message) {
        super(message);
    }
}
