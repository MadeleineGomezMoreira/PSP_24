package com.example.server.data.dao;

import com.example.server.domain.model.DriverCredential;

public interface DaoCredentials {
    DriverCredential getCredential(DriverCredential credential);

    DriverCredential getCredentialById(DriverCredential credential);

    DriverCredential getCredentialByEmail(DriverCredential credential);

    boolean update(DriverCredential credential);

}
