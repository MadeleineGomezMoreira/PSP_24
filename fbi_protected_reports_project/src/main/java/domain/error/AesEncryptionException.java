package domain.error;

public class AesEncryptionException extends RuntimeException {

    public AesEncryptionException(String message) {
        super(message);
    }
}
