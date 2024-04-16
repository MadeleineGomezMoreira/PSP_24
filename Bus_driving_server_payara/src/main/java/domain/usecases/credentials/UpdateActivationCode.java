package domain.usecases.credentials;

import data.dao.DaoCredentials;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

public class UpdateActivationCode {

    private final DaoCredentials dao;

    @Inject
    public UpdateActivationCode(DaoCredentials dao) {
        this.dao = dao;
    }

    public boolean updateActivationCode(DriverCredential credential) {
        return dao.updateActivationCode(credential);
    }
}
