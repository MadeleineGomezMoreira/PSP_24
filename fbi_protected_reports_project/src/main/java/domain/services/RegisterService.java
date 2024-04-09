package domain.services;

import domain.error.AppError;
import io.vavr.control.Either;

public interface RegisterService {
    Either<AppError, Integer> registerUser(String username, String password);
}
