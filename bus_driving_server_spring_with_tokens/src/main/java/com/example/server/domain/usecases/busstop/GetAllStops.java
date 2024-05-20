package com.example.server.domain.usecases.busstop;

import com.example.server.data.dao.DaoStops;
import com.example.server.domain.model.BusStop;
import jakarta.inject.Inject;

import java.util.List;

public class GetAllStops {

    private final DaoStops dao;

    @Inject
    public GetAllStops(DaoStops dao) {
        this.dao = dao;
    }

    public List<BusStop> getAll() {
        return dao.getAll();
    }
}
