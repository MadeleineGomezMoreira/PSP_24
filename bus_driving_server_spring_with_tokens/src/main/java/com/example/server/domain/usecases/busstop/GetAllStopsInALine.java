package com.example.server.domain.usecases.busstop;

import com.example.server.data.dao.DaoStops;
import com.example.server.domain.model.BusStop;
import jakarta.inject.Inject;

import java.util.List;

public class GetAllStopsInALine {

    private final DaoStops dao;

    @Inject
    public GetAllStopsInALine(DaoStops dao) {
        this.dao = dao;
    }

    public List<BusStop> getAllInALine(int busLineId) {
        return dao.getAll(new BusStop(busLineId));
    }

}
