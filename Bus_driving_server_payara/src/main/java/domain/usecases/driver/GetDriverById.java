package domain.usecases.driver;

import common.Constants;
import data.dao.DaoDrivers;
import domain.exception.DataValidationException;
import domain.model.BusDriver;
import jakarta.inject.Inject;

public class GetDriverById {

    private final DaoDrivers dao;

    @Inject
    public GetDriverById(DaoDrivers dao) {
        this.dao = dao;
    }

    public BusDriver get(int id) {
        if (id <= 0) {
            throw new DataValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
        BusDriver driver = new BusDriver(id);
        return dao.get(driver);
    }
}
