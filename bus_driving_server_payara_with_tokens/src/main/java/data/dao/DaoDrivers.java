package data.dao;

import domain.model.BusDriver;

import java.util.List;

public interface DaoDrivers {
    List<BusDriver> getAll();

    BusDriver get(BusDriver driver);

    Integer getAssignedLineId(BusDriver driver);

    Integer getId(BusDriver driver);

    BusDriver update(BusDriver driver);

    boolean delete(BusDriver driver);

    BusDriver save(BusDriver driver);
}
