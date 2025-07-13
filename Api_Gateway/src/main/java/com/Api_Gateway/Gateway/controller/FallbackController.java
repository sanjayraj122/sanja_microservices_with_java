package com.Api_Gateway.Gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth-service")
    public ResponseEntity<Map<String, Object>> authServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Authentication service is temporarily unavailable");
        response.put("message", "Please try again later or contact support if the issue persists");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("service", "auth-service");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/restaurant-service")
    @PostMapping("/restaurant-service")
    public ResponseEntity<Map<String, Object>> restaurantServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Restaurant service is temporarily unavailable");
        response.put("message", "Orders and restaurant data are temporarily unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("service", "restaurant-service");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/zomato-service")
    @PostMapping("/zomato-service")
    public ResponseEntity<Map<String, Object>> zomatoServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Zomato service is temporarily unavailable");
        response.put("message", "Order processing is temporarily unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("service", "zomato-service");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/general")
    @PostMapping("/general")
    public ResponseEntity<Map<String, Object>> generalFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Service temporarily unavailable");
        response.put("message", "The requested service is experiencing issues. Please try again later.");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("service", "general");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}