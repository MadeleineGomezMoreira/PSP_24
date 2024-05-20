package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.dao.DaoCredentials;
import com.example.server.domain.exception.AccountNotActivatedException;
import com.example.server.domain.exception.AuthenticationFailedException;
import com.example.server.domain.model.DriverCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAndGetRole {

    private final DaoCredentials dao;
    private final PasswordEncoder passwordEncoder;


    public String login(DriverCredential loginData) {
        DriverCredential credential = dao.getCredential(new DriverCredential(loginData.getUsername()));

        String inputPassword = loginData.getPassword();
        String storedPasswordWithSalt = credential.getPassword();

        boolean isPasswordCorrect = passwordEncoder.matches(inputPassword, storedPasswordWithSalt);

        if (!isPasswordCorrect) {
            throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_PASSWORD_ERROR);
        }

        if (!credential.isActivated()) {
            throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
        } else {
            return credential.getRole().getRoleName();
        }
    }

}
