package org.example.security_project_encripting.domain.model.error;

public class InvalidPrivateKeyException extends RuntimeException {

    public InvalidPrivateKeyException(String message) {
        super(message);
    }
}
