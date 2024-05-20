package com.example.server.domain.exception;

public class InsertFailedException extends RuntimeException {

    public InsertFailedException(String message) {
        super(message);
    }
}
