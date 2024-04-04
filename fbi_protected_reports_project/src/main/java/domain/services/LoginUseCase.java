package domain.services;

import data.dao.DaoUser;
import data.model.UserEntity;
import domain.error.AppError;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import ui.security.KeyRetrieverTool;
import ui.security.PasswordManager;

import java.util.Arrays;

public class LoginUseCase {

    private final DaoUser daoUser;
    private final PasswordManager passwordManager;

    @Inject
    public LoginUseCase(DaoUser daoUser, PasswordManager passwordManager) {
        this.daoUser = daoUser;
        this.passwordManager = passwordManager;
    }

    public Either<AppError, Boolean> login(UserEntity user) {
        Either<AppError, Boolean> result;
        try {
            //retrieve user by username from db
            Either<AppError, UserEntity> getUser = daoUser.get(user);

            if (getUser.isLeft()) {
                result = Either.left(getUser.getLeft());
            } else {
                UserEntity userFromDb = getUser.get();
                //hash the input password and compare it with the one in the db
                char[] passHashed = passwordManager.hashPassword(user.getPassword(), userFromDb.getSalt());
                char[] passHashedDb = passwordManager.hashPassword(userFromDb.getPassword(), userFromDb.getSalt());
                //if the passwords match, return true
                if (Arrays.equals(passHashed, passHashedDb)) {
                    result = Either.right(true);
                } else {
                    result = Either.right(false);
                }
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }
}
