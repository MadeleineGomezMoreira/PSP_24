package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.dao.DaoDrivers;
import com.example.server.data.dao.DaoLines;
import com.example.server.domain.exception.DriverValidationException;
import com.example.server.domain.model.BusDriver;
import com.example.server.domain.model.BusLine;
import com.example.server.domain.model.DriverCredential;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

public class RegisterAccount {

    private final DaoDrivers dao;
    private final DaoLines daoLines;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public RegisterAccount(DaoDrivers dao, DaoLines daoLines, Pbkdf2PasswordHash passwordHash) {
        this.dao = dao;
        this.daoLines = daoLines;
        this.passwordHash = passwordHash;
    }

    public void registerDriver(BusDriver driver) {
        if (driver != null) {
            String hashedPassword = (passwordHash.generate(driver.getCredential().getPassword().toCharArray()));

            BusLine busLine = daoLines.get(new BusLine(0));

            DriverCredential credential = driver.getCredential();
            credential.setPassword(hashedPassword);
            driver.setAssignedLine(busLine);
            driver.setCredential(credential);

            dao.save(driver);
        } else {
            throw new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
    }
}
