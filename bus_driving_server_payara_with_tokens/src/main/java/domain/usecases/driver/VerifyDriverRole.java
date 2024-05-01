package domain.usecases.driver;

import common.Constants;
import data.dao.DaoCredentials;
import domain.dto.CredentialVerificationDTO;
import domain.exception.RoleValidationException;
import domain.model.DriverCredential;
import jakarta.inject.Inject;

public class VerifyDriverRole {

    private final DaoCredentials dao;

    @Inject
    public VerifyDriverRole(DaoCredentials dao) {
        this.dao = dao;
    }

    public void verifyRole(CredentialVerificationDTO credential) {
        DriverCredential driverCredential = dao.getCredential(new DriverCredential(credential.getUsername()));

        if (!driverCredential.getRole().getRoleName().equals(credential.getRole().getRoleName())) {
            throw new RoleValidationException(Constants.ACCESS_DENIED_INCORRECT_ROLE);
        }
    }
}
