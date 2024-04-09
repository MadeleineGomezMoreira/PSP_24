package domain.services;

import data.model.UserEntity;
import domain.error.AppError;
import io.vavr.control.Either;

public interface LoginService {
    Either<AppError, Boolean> login(UserEntity user);
}
