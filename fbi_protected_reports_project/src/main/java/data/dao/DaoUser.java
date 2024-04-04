package data.dao;

import data.model.UserEntity;
import domain.error.AppError;
import io.vavr.control.Either;

public interface DaoUser {
    //get user by username
    Either<AppError, UserEntity> get(UserEntity user);

    //get user by id
    Either<AppError, UserEntity> get(int id);

    //save new user
    Either<AppError, Integer> save(UserEntity user);
}
