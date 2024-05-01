package domain.usecases.busstop;

import data.dao.DaoStops;
import domain.model.BusStop;
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
