package domain.usecases.driver;

import common.Constants;
import data.dao.DaoDrivers;
import data.dao.DaoLines;
import domain.dto.RegisterDTO;
import domain.exception.DriverValidationException;
import domain.model.BusDriver;
import domain.model.BusLine;
import domain.model.DriverCredential;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

public class RegisterDriver {

    private final DaoDrivers dao;
    private final DaoLines daoLines;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public RegisterDriver(DaoDrivers dao, DaoLines daoLines, Pbkdf2PasswordHash passwordHash) {
        this.dao = dao;
        this.daoLines = daoLines;
        this.passwordHash = passwordHash;
    }

    public BusDriver registerDriver(RegisterDTO driver) {
        if (driver != null) {
            String hashedPassword = (passwordHash.generate(driver.getPassword().toCharArray()));

            BusLine busLine = daoLines.get(new BusLine(0));

            BusDriver busDriver = new BusDriver();
            DriverCredential credential = new DriverCredential();

            busDriver.setFirstName(driver.getFirstName());
            busDriver.setLastName(driver.getLastName());
            busDriver.setPhone(driver.getPhone());
            credential.setEmail(driver.getEmail());
            credential.setUsername(driver.getUsername());
            credential.setPassword(hashedPassword);
            credential.setActivationCode(driver.getActivationCode());
            credential.setActivationDate(driver.getActivationDate());
            busDriver.setCredential(credential);
            busDriver.setAssignedLine(busLine);

            return dao.save(busDriver);
        } else {
            throw new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
    }
}
