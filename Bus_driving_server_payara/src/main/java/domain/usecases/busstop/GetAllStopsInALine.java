package domain.usecases.busstop;

import data.dao.DaoStops;
import domain.model.BusStop;
import domain.model.Point;
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
