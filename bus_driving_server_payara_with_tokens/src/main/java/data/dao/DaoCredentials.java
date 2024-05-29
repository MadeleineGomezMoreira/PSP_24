package data.dao;

import domain.model.DriverCredential;

public interface DaoCredentials {

    DriverCredential getCredential(DriverCredential credential);

    DriverCredential getCredentialById(DriverCredential credential);

    DriverCredential getCredentialByEmail(DriverCredential credential);

    boolean update(DriverCredential credential);

}
