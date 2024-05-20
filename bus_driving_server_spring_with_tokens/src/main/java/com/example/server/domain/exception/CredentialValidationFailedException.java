package com.example.server.domain.exception;

public class CredentialValidationFailedException extends RuntimeException {

    public CredentialValidationFailedException(String message) {
        super(message);
    }
}
