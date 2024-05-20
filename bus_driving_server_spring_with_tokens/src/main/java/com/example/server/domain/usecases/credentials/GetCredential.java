package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.dao.DaoCredentials;
import com.example.server.domain.exception.AccountNotActivatedException;
import com.example.server.domain.model.DriverCredential;
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
        } else {
            return credential;
        }
    }
}
