package domain.exception;

public class CredentialValidationFailedException extends RuntimeException {

    public CredentialValidationFailedException(String message) {
        super(message);
    }
}
