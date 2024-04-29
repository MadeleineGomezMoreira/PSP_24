package data.dao;

import domain.model.BusStop;

import java.util.List;

public interface DaoStops {
    //get all bus stops from a specific line
    List<BusStop> getAll(BusStop busStop);

    //get all bus stops
    List<BusStop> getAll();

    //get a specific bus stop
    BusStop get(BusStop busStop);
}
