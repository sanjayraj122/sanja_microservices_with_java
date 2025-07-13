package com.Auth_service.Auth.exception;

import lombok.Data;

@Data
public class UsernameNotFoundException extends Exception{
    private String username;

    public UsernameNotFoundException(String username) {
        super(String.format("Username not found: %s", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
