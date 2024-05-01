package domain.usecases.driver;

import data.dao.impl.DaoDriversImpl;
import domain.model.BusDriver;
import jakarta.inject.Inject;

import java.util.List;

public class GetAllDrivers {

    private final DaoDriversImpl dao;

    @Inject
    public GetAllDrivers(DaoDriversImpl dao) {
        this.dao = dao;
    }

    public List<BusDriver> getAll() {
        return dao.getAll();
    }
}
