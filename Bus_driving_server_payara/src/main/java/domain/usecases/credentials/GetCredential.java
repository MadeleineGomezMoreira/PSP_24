package domain.usecases.credentials;

import common.Constants;
import data.dao.DaoCredentials;
import domain.exception.AccountNotActivatedException;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

public class GetCredential {

    private final DaoCredentials dao;

    @Inject
    public GetCredential(DaoCredentials dao) {
        this.dao = dao;
    }

    public DriverCredential getCredential(String username) {
        DriverCredential credential = dao.getCredential(new DriverCredential(username));
        if (!credential.isActivated()) {
            throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
        } else{
            return credential;
        }
    }
}
