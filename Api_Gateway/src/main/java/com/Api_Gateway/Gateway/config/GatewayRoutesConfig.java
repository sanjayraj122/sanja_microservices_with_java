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
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://ZOMATO-APP-SERVICE"))

                .route("restaurant-service", r -> r.path("/orders/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://RESTAURANT-SERVICE"))

                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://AUTH-SERVICE")) // no filter applied here
                .build();
    }
}
