package domain.usecases.driver;

import data.dao.DaoDrivers;
import domain.model.BusDriver;
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
