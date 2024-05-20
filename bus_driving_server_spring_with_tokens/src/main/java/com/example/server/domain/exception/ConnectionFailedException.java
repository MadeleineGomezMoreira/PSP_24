package com.example.server.domain.exception;

public class ConnectionFailedException extends RuntimeException {

    public ConnectionFailedException(String message) {
        super(message);
    }
}
