package domain.services;

import data.dao.DaoUser;
import data.model.UserEntity;
import domain.error.AppError;
import domain.model.PasswordItem;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import ui.security.KeySaverTool;
import ui.security.PasswordManager;

public class RegisterUseCase {

    private final DaoUser daoUser;
    private final PasswordManager passwordManager;
    private final KeySaverTool keySaverTool;

    @Inject
    public RegisterUseCase(DaoUser daoUser, PasswordManager passwordManager, KeySaverTool keySaverTool) {
        this.daoUser = daoUser;
        this.passwordManager = passwordManager;
        this.keySaverTool = keySaverTool;
    }

    public Either<AppError, Integer> registerUser(String username, String password) {
        Either<AppError, Integer> result;
        try {
            //we hash the password with a random salt
            PasswordItem passwordItem = passwordManager.hashPasswordRandomSalt(password);

            //we save the user in the database
            Either<AppError, Integer> saveUser = daoUser.save(new UserEntity(0, username, passwordItem.getPassword(), passwordItem.getSalt()));

            //we get the admin from db
            Either<AppError, UserEntity> getAdmin = daoUser.get(-1);

            if (saveUser.isLeft()) {
                result = Either.left(saveUser.getLeft());
            } else {
                    UserEntity admin = getAdmin.get();
                    keySaverTool.saveUserToKeyStore("keystore.pfx", password, username, "admin");

                    result = Either.right(saveUser.get());
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

}
