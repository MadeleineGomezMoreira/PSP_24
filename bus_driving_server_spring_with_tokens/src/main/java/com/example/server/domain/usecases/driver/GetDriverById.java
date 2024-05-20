package com.example.server.domain.usecases.driver;

import com.example.server.common.Constants;
import com.example.server.data.dao.DaoDrivers;
import com.example.server.domain.exception.DataValidationException;
import com.example.server.domain.model.BusDriver;
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
