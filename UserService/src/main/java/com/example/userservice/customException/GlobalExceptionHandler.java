package com.example.userservice.customException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<Map<String,Object>> handlePasswordNotMatchException(PasswordNotMatchException passwordNotMatchException) {
        Map<String,Object> map=new HashMap<>();
        map.put("password", passwordNotMatchException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(map);
    }
}
