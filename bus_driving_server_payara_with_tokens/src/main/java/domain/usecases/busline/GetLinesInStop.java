package domain.usecases.busline;

import data.dao.DaoLines;
import domain.model.BusLine;
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
