package domain.exception;

public class InsertFailedException extends RuntimeException {

    public InsertFailedException(String message) {
        super(message);
    }
}
