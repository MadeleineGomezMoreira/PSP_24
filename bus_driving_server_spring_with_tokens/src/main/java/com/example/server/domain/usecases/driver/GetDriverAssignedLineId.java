package com.example.server.domain.usecases.driver;

import com.example.server.data.dao.DaoDrivers;
import com.example.server.domain.model.BusDriver;
import jakarta.inject.Inject;

public class GetDriverAssignedLineId {

    private final DaoDrivers daoDrivers;

    @Inject
    public GetDriverAssignedLineId(DaoDrivers daoDrivers) {
        this.daoDrivers = daoDrivers;
    }

    public Integer getAssignedLineId(int driverId) {
        return daoDrivers.getAssignedLineId(new BusDriver(driverId));
    }


}
