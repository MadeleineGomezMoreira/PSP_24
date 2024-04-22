package domain.usecases.credentials;

import common.Constants;
import data.dao.DaoCredentials;
import domain.exception.AccountAlreadyActivatedException;
import domain.exception.ActivationFailedException;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

        try {

            //check if the activation code is suitable (not expired) - expires 5 minutes after creation
            //check against now() = java.time.LocalDateTime.now()
            //check against activationDate + 2 hours (because of the time difference in the mysql database)
            LocalDateTime activationDate = c.getActivationDate().plusHours(2);
            String decodedActivationCode = URLDecoder.decode(activationCode, "UTF-8");
            boolean codeIsExpired = activationDate.plusMinutes(5).isBefore(java.time.LocalDateTime.now());
            boolean codeIsCorrect = decodedActivationCode.equals(activationCode);

            if (!codeIsExpired && codeIsCorrect) {
                //activate the account
                c.setActivated(true);
                return dao.update(c);
            } else {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            throw new ActivationFailedException(Constants.ACTIVATION_FAILED_ENCODING_ERROR);
        }
    }
}
