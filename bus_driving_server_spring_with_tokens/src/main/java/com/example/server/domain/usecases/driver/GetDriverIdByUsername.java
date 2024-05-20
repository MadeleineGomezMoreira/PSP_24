package com.example.server.domain.usecases.driver;

import com.example.server.data.dao.DaoDrivers;
import com.example.server.domain.model.BusDriver;
import jakarta.inject.Inject;

public class GetDriverIdByUsername {
    private final DaoDrivers daoDrivers;

    @Inject
    public GetDriverIdByUsername(DaoDrivers daoDrivers) {
        this.daoDrivers = daoDrivers;
    }

    public int getDriverIdByUsername(String username) {
        return daoDrivers.getId(new BusDriver(username));
    }
}
