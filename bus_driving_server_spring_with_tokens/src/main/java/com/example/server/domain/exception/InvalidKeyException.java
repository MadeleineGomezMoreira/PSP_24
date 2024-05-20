package com.example.server.domain.exception;

public class InvalidKeyException extends RuntimeException {

    public InvalidKeyException(String message) {
        super(message);
    }
}
