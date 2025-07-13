package com.restaurant_service.restaurant.exception;

import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
     }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleNotFoundException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

}
