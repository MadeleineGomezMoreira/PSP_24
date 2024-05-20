package com.example.server.domain.exception;

public class DriverValidationException extends RuntimeException {

    public DriverValidationException(String message) {
        super(message);
    }
}
