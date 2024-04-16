package domain.usecases.credentials;

import data.dao.DaoCredentials;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

public class VerifyCredential {

    private final DaoCredentials dao;

    @Inject
    public VerifyCredential(DaoCredentials dao) {
        this.dao = dao;
    }

    public boolean verify(DriverCredential credential) {
        return dao.verifyCredential(credential);
    }

}
