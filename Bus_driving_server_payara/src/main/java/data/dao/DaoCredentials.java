package data.dao;

import domain.model.DriverCredential;

public interface DaoCredentials {
    DriverCredential getCredential(DriverCredential credential);

    boolean activateCredential(DriverCredential credential);

    boolean updateActivationCode(DriverCredential credential);

    boolean verifyCredential(DriverCredential credential);
}
