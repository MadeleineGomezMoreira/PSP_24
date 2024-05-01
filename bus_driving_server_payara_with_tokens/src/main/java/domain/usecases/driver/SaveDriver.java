package domain.usecases.driver;

import common.Constants;
import data.dao.DaoDrivers;
import data.dao.DaoLines;
import domain.exception.DriverValidationException;
import domain.model.BusDriver;
import jakarta.inject.Inject;

public class SaveDriver {

    private final DaoDrivers dao;
    private final DaoLines daoLines;

    @Inject
    public SaveDriver(DaoDrivers dao, DaoLines daoLines) {
        this.dao = dao;
        this.daoLines = daoLines;
    }

    public BusDriver save(BusDriver driver) {
        if (driver != null) {
            return dao.save(driver);
        } else {
            throw new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
    }
}
