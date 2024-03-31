package domain.error;

public class InvalidPrivateKeyException extends RuntimeException {

    public InvalidPrivateKeyException(String message) {
        super(message);
    }
}
