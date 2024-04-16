package domain.usecases.driver;

import common.Constants;

import data.dao.impl.DaoDriversImpl;
import domain.exception.DriverValidationException;
import domain.model.BusDriver;
import jakarta.inject.Inject;

public class DeleteDriver {

    private final DaoDriversImpl dao;

    @Inject
    public DeleteDriver(DaoDriversImpl dao) {
        this.dao = dao;
    }

    public boolean delete(int id) {
        if (id <= 0) {
            throw new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
        BusDriver driver = new BusDriver(id);
        return dao.delete(driver);
    }
}
