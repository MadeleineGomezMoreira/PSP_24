package domain.exception;

public class ActivationFailedException extends RuntimeException {

    public ActivationFailedException(String message) {
        super(message);
    }
}
