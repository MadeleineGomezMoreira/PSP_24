package domain.usecases.busstop;

import data.dao.DaoStops;
import domain.model.BusStop;
import domain.model.Point;
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
