package com.Api_Gateway.Gateway.config;

import com.Api_Gateway.Gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    private final AuthenticationFilter authenticationFilter;

    public GatewayRoutesConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("zomato-app-service", r -> r.path("/zomato/orders/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .circuitBreaker(config -> config
                                        .setName("zomato-service")
                                        .setFallbackUri("forward:/fallback/zomato-service"))
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods("GET", "POST")
                                        .setBackoff(java.time.Duration.ofMillis(100), java.time.Duration.ofMillis(2000), 2, false)))
                        .uri("lb://ZOMATO-APP-SERVICE"))

                .route("restaurant-service", r -> r.path("/orders/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .circuitBreaker(config -> config
                                        .setName("restaurant-service")
                                        .setFallbackUri("forward:/fallback/restaurant-service"))
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods("GET", "POST")
                                        .setBackoff(java.time.Duration.ofMillis(100), java.time.Duration.ofMillis(1500), 2, false)))
                        .uri("lb://RESTAURANT-SERVICE"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("auth-service")
                                        .setFallbackUri("forward:/fallback/auth-service"))
                                .retry(retryConfig -> retryConfig
                                        .setRetries(2)
                                        .setMethods("GET", "POST")
                                        .setBackoff(java.time.Duration.ofMillis(50), java.time.Duration.ofMillis(1000), 2, false)))
                        .uri("lb://AUTH-SERVICE")) // no auth filter applied here
                .build();
    }
}
