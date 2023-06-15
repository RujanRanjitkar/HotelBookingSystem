package com.example.apigateway.customException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MustLoginException.class)
    public ResponseEntity<Map<String, Object>> handleMustLoginException(MustLoginException mustLoginException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", mustLoginException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }

    @ExceptionHandler(RoleNotMatchException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotMatchException(RoleNotMatchException roleNotMatchException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", roleNotMatchException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}
