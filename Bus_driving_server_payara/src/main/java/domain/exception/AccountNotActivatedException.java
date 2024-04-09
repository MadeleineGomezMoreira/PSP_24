package domain.exception;

public class AccountNotActivatedException extends RuntimeException {

    public AccountNotActivatedException(String message) {
        super(message);
    }
}
