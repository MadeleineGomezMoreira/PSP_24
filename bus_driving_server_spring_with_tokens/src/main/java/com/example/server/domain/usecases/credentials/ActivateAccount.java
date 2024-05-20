package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.dao.DaoCredentials;
import com.example.server.domain.exception.AccountAlreadyActivatedException;
import com.example.server.domain.model.DriverCredential;
import jakarta.inject.Inject;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ActivateAccount {
    private final DaoCredentials dao;

    @Inject
    public ActivateAccount(DaoCredentials dao) {
        this.dao = dao;
    }

    public boolean activateAccount(String email, String activationCode) {
        //get the DriverCredential object
        DriverCredential c = dao.getCredentialByEmail(new DriverCredential(email, null, null));

        //check if account is already activated
        if (c.isActivated()) {
            throw new AccountAlreadyActivatedException(Constants.ACCOUNT_ALREADY_ACTIVATED);
        }

        //check if the activation code is suitable (not expired) - expires 5 minutes after creation
        //check against now() = java.time.LocalDateTime.now()
        //check against activationDate + 2 hours (because of the time difference in the mysql database)
        LocalDateTime activationDate = c.getActivationDate().plusHours(2);
        String decodedActivationCode = URLDecoder.decode(activationCode, StandardCharsets.UTF_8);
        //replace any spaces in the activation code for "+"
        decodedActivationCode = decodedActivationCode.replace(" ", "+");
        boolean codeIsExpired = activationDate.plusMinutes(5).isBefore(LocalDateTime.now());
        boolean codeIsCorrect = decodedActivationCode.equals(activationCode);

        if (!codeIsExpired && codeIsCorrect) {
            //activate the account
            c.setActivated(true);
            return dao.update(c);
        } else {
            return false;
        }
    }
}
