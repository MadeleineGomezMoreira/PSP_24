package domain.error;

public class AesDecryptionException extends RuntimeException {

    public AesDecryptionException(String message) {
        super(message);
    }
}
