package domain.error;

public class RsaDecryptionException extends RuntimeException {

    public RsaDecryptionException(String message) {
        super(message);
    }
}
