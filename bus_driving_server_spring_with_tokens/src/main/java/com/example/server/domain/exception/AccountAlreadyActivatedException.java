package com.example.server.domain.exception;

public class AccountAlreadyActivatedException extends RuntimeException {

    public AccountAlreadyActivatedException(String message) {
        super(message);
    }
}
