package com.example.server.domain.usecases.driver;

import com.example.server.common.Constants;
import com.example.server.data.dao.DaoCredentials;
import com.example.server.domain.dto.CredentialVerificationDTO;
import com.example.server.domain.exception.RoleValidationException;
import com.example.server.domain.model.DriverCredential;
import jakarta.inject.Inject;

public class VerifyDriverRole {

    private final DaoCredentials dao;

    @Inject
    public VerifyDriverRole(DaoCredentials dao) {
        this.dao = dao;
    }

    public void verifyRole(CredentialVerificationDTO credential) {
        DriverCredential driverCredential = dao.getCredential(new DriverCredential(credential.getUsername()));

        if (!driverCredential.getRole().getRoleName().equals(credential.getRole())) {
            throw new RoleValidationException(Constants.ACCESS_DENIED_INCORRECT_ROLE);
        }
    }
}
