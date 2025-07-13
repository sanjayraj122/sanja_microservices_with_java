package com.Api_Gateway.Gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfig {

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(20)
                        .permittedNumberOfCallsInHalfOpenState(5)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofMillis(30000))
                        .slowCallRateThreshold(50)
                        .slowCallDurationThreshold(Duration.ofMillis(2000))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofMillis(3000))
                        .build())
                .build());
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> authServiceCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .failureRateThreshold(60)
                        .waitDurationInOpenState(Duration.ofMillis(20000))
                        .slowCallRateThreshold(60)
                        .slowCallDurationThreshold(Duration.ofMillis(1000))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofMillis(2000))
                        .build()), "auth-service");
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> restaurantServiceCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(15)
                        .permittedNumberOfCallsInHalfOpenState(4)
                        .failureRateThreshold(55)
                        .waitDurationInOpenState(Duration.ofMillis(25000))
                        .slowCallRateThreshold(55)
                        .slowCallDurationThreshold(Duration.ofMillis(1500))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofMillis(2500))
                        .build()), "restaurant-service");
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> zomatoServiceCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(25)
                        .permittedNumberOfCallsInHalfOpenState(6)
                        .failureRateThreshold(45)
                        .waitDurationInOpenState(Duration.ofMillis(35000))
                        .slowCallRateThreshold(45)
                        .slowCallDurationThreshold(Duration.ofMillis(3000))
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofMillis(4000))
                        .build()), "zomato-service");
    }
}