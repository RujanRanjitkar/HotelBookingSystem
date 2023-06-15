package com.example.bookingservice.customException;

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

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleRoomNotAvailableException(RoomNotAvailableException roomNotAvailableException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", roomNotAvailableException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExistsException(RoomNotFoundException roomNotFoundException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", roomNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(DateMisMatchException.class)
    public ResponseEntity<Map<String, Object>> handleDateMisMatchException(DateMisMatchException dateMisMatchException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", dateMisMatchException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
