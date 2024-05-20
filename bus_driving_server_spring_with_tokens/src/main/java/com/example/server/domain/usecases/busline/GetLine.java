package com.example.server.domain.usecases.busline;

import com.example.server.data.dao.DaoLines;
import com.example.server.domain.model.BusLine;
import jakarta.inject.Inject;

public class GetLine {

    private final DaoLines dao;

    @Inject
    public GetLine(DaoLines dao) {
        this.dao = dao;
    }

    public BusLine get(int lineId) {
        return dao.get(new BusLine(lineId));
    }
}
