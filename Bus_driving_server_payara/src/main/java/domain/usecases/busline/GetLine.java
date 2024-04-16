package domain.usecases.busline;

import data.dao.impl.DaoLinesImpl;
import domain.model.BusLine;
import jakarta.inject.Inject;

import java.util.List;

public class GetLine {

    private final DaoLinesImpl dao;

    @Inject
    public GetLine(DaoLinesImpl dao) {
        this.dao = dao;
    }

    public BusLine get(int lineId) {
        return dao.get(new BusLine(lineId));
    }
}
