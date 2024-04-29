package domain.usecases.busline;

import data.dao.DaoLines;
import domain.model.BusLine;
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
