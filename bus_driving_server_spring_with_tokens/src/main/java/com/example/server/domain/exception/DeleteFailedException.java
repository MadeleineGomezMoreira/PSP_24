package com.example.server.domain.exception;

public class DeleteFailedException extends RuntimeException {

    public DeleteFailedException(String message) {
        super(message);
    }
}
