package com.example.server.domain.usecases.busstop;

import com.example.server.data.dao.DaoStops;
import com.example.server.domain.model.BusStop;
import jakarta.inject.Inject;

public class GetStop {

    private final DaoStops dao;

    @Inject
    public GetStop(DaoStops dao) {
        this.dao = dao;
    }

    public BusStop get(int lineId) {
        return dao.get(new BusStop(lineId));
    }

}
