package com.example.server.domain.usecases.driver;

import com.example.server.common.Constants;
import com.example.server.data.dao.impl.DaoDriversImpl;
import com.example.server.domain.exception.DriverValidationException;
import com.example.server.domain.model.BusDriver;
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
