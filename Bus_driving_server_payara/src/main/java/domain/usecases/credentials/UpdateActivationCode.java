package domain.usecases.credentials;

import data.dao.DaoCredentials;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

public class UpdateActivationCode {

    private final DaoCredentials dao;

    @Inject
    public UpdateActivationCode(DaoCredentials dao) {
        this.dao = dao;
    }

    public boolean updateActivationCode(String email, String activationCode) {
        //get the DriverCredential object
        DriverCredential c = dao.getCredentialByEmail(new DriverCredential(email, null, null));
        //update it
        c.setActivationCode(activationCode);
        c.setActivationDate(LocalDateTime.now());
        return dao.update(c);
    }
}
