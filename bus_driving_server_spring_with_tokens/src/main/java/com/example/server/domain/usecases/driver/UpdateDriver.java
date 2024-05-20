package com.example.server.domain.usecases.driver;

import com.example.server.common.Constants;
import com.example.server.data.dao.impl.DaoDriversImpl;
import com.example.server.domain.exception.DriverValidationException;
import com.example.server.domain.model.BusDriver;
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
