package data.dao;

import domain.model.DriverCredential;

public interface DaoCredentials {
    DriverCredential getCredential(DriverCredential credential);

    DriverCredential getCredentialByEmail(DriverCredential credential);

    boolean update(DriverCredential credential);

}
