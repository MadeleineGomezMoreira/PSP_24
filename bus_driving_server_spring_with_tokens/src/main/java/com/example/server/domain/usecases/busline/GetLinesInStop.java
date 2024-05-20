package com.example.server.domain.usecases.busline;

import com.example.server.data.dao.DaoLines;
import com.example.server.domain.model.BusLine;
import jakarta.inject.Inject;

import java.util.List;

public class GetLinesInStop {
    private final DaoLines dao;

    @Inject
    public GetLinesInStop(DaoLines dao) {
        this.dao = dao;
    }

    public List<BusLine> getAll(int stopId) {
        return dao.getAll(new BusLine(stopId));
    }

}
