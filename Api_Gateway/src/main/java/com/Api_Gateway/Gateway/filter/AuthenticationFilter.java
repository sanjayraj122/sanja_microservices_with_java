package com.Api_Gateway.Gateway.filter;

import com.Api_Gateway.Gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                // 1. Check for missing Authorization header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    System.out.println("Missing Authorization header");
                    return exchange.getResponse().setComplete();
                }

                // 2. Extract token from header
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    System.out.println("Unauthorized request: Invalid Authorization header format");
                    return exchange.getResponse().setComplete();
                }

                String token = authHeader.substring(7);

                // 3. Validate token
                try {
                    jwtUtil.validateToken(token);
                    System.out.println("Token is valid");
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    System.out.println("token validation failed: " + e.getMessage());
                    return exchange.getResponse().setComplete();
                }
            }

            // Continue the request if valid
            return chain.filter(exchange);
        };
    }


    public static class Config {

    }
}
