package domain.usecases.driver;

import common.Constants;
import data.dao.impl.DaoDriversImpl;
import domain.exception.DriverValidationException;
import domain.model.BusDriver;
import jakarta.inject.Inject;

public class UpdateDriver {

    private final DaoDriversImpl dao;

    @Inject
    public UpdateDriver(DaoDriversImpl dao) {
        this.dao = dao;
    }

    public BusDriver update(BusDriver driver) {
        if (driver != null) {
            return dao.update(driver);
        } else {
            throw new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
    }
}
