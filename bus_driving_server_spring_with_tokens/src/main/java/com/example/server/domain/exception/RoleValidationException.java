package com.example.server.domain.exception;

public class RoleValidationException extends RuntimeException {

    public RoleValidationException(String message) {
        super(message);
    }
}
