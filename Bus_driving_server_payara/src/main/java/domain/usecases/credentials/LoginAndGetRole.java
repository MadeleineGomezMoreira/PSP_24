package domain.usecases.credentials;

import common.Constants;
import data.dao.DaoCredentials;
import domain.dto.LoginDTO;
import domain.exception.AccountNotActivatedException;
import domain.exception.AuthenticationFailedException;
import domain.model.DriverCredential;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;

public class LoginAndGetRole {

    private final DaoCredentials dao;
    private final PasswordHash passwordHash;

    @Inject
    public LoginAndGetRole(DaoCredentials dao, PasswordHash passwordHash) {
        this.dao = dao;
        this.passwordHash = passwordHash;
    }

    public String login(LoginDTO loginData) {
        DriverCredential credential = dao.getCredential(new DriverCredential(loginData.getUsername()));

        String inputPassword = loginData.getPassword();
        String storedPasswordWithSalt = credential.getPassword();

        boolean isPasswordCorrect = passwordHash.verify(inputPassword.toCharArray(), storedPasswordWithSalt);

        if(!isPasswordCorrect){
            throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_PASSWORD_ERROR);
        }

        if (!credential.isActivated()) {
            throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
        } else{
            return credential.getRole().getRoleName();
        }
    }

}
