package domain.error;

public class RsaEncryptionException extends RuntimeException {

    public RsaEncryptionException(String message) {
        super(message);
    }
}
