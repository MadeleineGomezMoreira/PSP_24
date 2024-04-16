package domain.usecases.credentials;

import data.dao.DaoCredentials;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

public class GetCredential {

    private final DaoCredentials dao;

    @Inject
    public GetCredential(DaoCredentials dao) {
        this.dao = dao;
    }

    public DriverCredential getCredential(String username) {
        return dao.getCredential(new DriverCredential(username));
    }
}
