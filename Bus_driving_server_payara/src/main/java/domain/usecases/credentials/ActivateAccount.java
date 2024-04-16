package domain.usecases.credentials;

import data.dao.DaoCredentials;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

public class ActivateAccount {
    private final DaoCredentials dao;
    @Inject
    public ActivateAccount(DaoCredentials dao) {
        this.dao = dao;
    }

    public boolean activateAccount(DriverCredential credential) {
        return dao.activateCredential(credential);
    }
}
