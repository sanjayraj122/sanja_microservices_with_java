package com.Auth_service.Auth.exception;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String resourceField;
    private Long fieldValue;

    public ResourceNotFoundException(String resourceName, String resourceField, Long fieldValue) {
        super(String.format("%s not found %s: %s",resourceName,resourceField,fieldValue));
        this.resourceName = resourceName;
        this.resourceField = resourceField;
        this.fieldValue = fieldValue;
    }
}
