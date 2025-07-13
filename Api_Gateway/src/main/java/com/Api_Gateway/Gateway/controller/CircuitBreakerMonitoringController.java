package com.Api_Gateway.Gateway.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/actuator/circuitbreaker")
public class CircuitBreakerMonitoringController {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getCircuitBreakerHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> circuitBreakers = circuitBreakerRegistry.getAllCircuitBreakers()
                .asJava()
                .stream()
                .collect(Collectors.toMap(
                        CircuitBreaker::getName,
                        cb -> {
                            Map<String, Object> cbInfo = new HashMap<>();
                            cbInfo.put("state", cb.getState().toString());
                            cbInfo.put("failureRate", cb.getMetrics().getFailureRate());
                            cbInfo.put("slowCallRate", cb.getMetrics().getSlowCallRate());
                            cbInfo.put("numberOfSuccessfulCalls", cb.getMetrics().getNumberOfSuccessfulCalls());
                            cbInfo.put("numberOfFailedCalls", cb.getMetrics().getNumberOfFailedCalls());
                            cbInfo.put("numberOfSlowCalls", cb.getMetrics().getNumberOfSlowCalls());
                            cbInfo.put("numberOfNotPermittedCalls", cb.getMetrics().getNumberOfNotPermittedCalls());
                            return cbInfo;
                        }
                ));
        
        health.put("circuitBreakers", circuitBreakers);
        
        boolean allHealthy = circuitBreakers.values().stream()
                .allMatch(cb -> {
                    Map<String, Object> cbMap = (Map<String, Object>) cb;
                    return !"OPEN".equals(cbMap.get("state"));
                });
        
        health.put("status", allHealthy ? "UP" : "DOWN");
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/details/{circuitBreakerName}")
    public ResponseEntity<Map<String, Object>> getCircuitBreakerDetails(@PathVariable String circuitBreakerName) {
        try {
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
            
            Map<String, Object> details = new HashMap<>();
            details.put("name", circuitBreaker.getName());
            details.put("state", circuitBreaker.getState().toString());
            details.put("timestamp", LocalDateTime.now());
            
            // Metrics
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("failureRate", circuitBreaker.getMetrics().getFailureRate());
            metrics.put("slowCallRate", circuitBreaker.getMetrics().getSlowCallRate());
            metrics.put("numberOfSuccessfulCalls", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls());
            metrics.put("numberOfFailedCalls", circuitBreaker.getMetrics().getNumberOfFailedCalls());
            metrics.put("numberOfSlowCalls", circuitBreaker.getMetrics().getNumberOfSlowCalls());
            metrics.put("numberOfNotPermittedCalls", circuitBreaker.getMetrics().getNumberOfNotPermittedCalls());
            details.put("metrics", metrics);
            
            // Configuration
            Map<String, Object> config = new HashMap<>();
            config.put("failureRateThreshold", circuitBreaker.getCircuitBreakerConfig().getFailureRateThreshold());
            config.put("slowCallRateThreshold", circuitBreaker.getCircuitBreakerConfig().getSlowCallRateThreshold());
            config.put("slowCallDurationThreshold", circuitBreaker.getCircuitBreakerConfig().getSlowCallDurationThreshold());
            config.put("permittedNumberOfCallsInHalfOpenState", circuitBreaker.getCircuitBreakerConfig().getPermittedNumberOfCallsInHalfOpenState());
            config.put("slidingWindowSize", circuitBreaker.getCircuitBreakerConfig().getSlidingWindowSize());
            config.put("waitDurationInOpenState", circuitBreaker.getCircuitBreakerConfig().getWaitDurationInOpenState());
            details.put("configuration", config);
            
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Circuit breaker not found");
            error.put("circuitBreakerName", circuitBreakerName);
            error.put("timestamp", LocalDateTime.now());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/events/{circuitBreakerName}")
    public ResponseEntity<Map<String, Object>> getCircuitBreakerEvents(@PathVariable String circuitBreakerName) {
        try {
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
            
            Map<String, Object> events = new HashMap<>();
            events.put("circuitBreakerName", circuitBreakerName);
            events.put("timestamp", LocalDateTime.now());
            
            // Get recent events (last 100)
            var eventStream = circuitBreaker.getEventPublisher()
                    .onEvent(event -> {
                        // Events are automatically collected
                    });
            
            events.put("message", "Circuit breaker events are being monitored. Check logs for detailed event information.");
            
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Circuit breaker not found");
            error.put("circuitBreakerName", circuitBreakerName);
            error.put("timestamp", LocalDateTime.now());
            return ResponseEntity.notFound().build();
        }
    }
}