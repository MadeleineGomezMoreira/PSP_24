package domain.error;

public class PrivateKeyNotFoundException extends RuntimeException {

    public PrivateKeyNotFoundException(String message) {
        super(message);
    }
}
