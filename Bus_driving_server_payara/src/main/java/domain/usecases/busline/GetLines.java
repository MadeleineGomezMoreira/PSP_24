package domain.usecases.busline;

import data.dao.impl.DaoLinesImpl;
import domain.model.BusLine;
import jakarta.inject.Inject;

import java.util.List;

public class GetLines {

    private final DaoLinesImpl dao;

    @Inject
    public GetLines(DaoLinesImpl dao) {
        this.dao = dao;
    }

    public List<BusLine> getAll() {
        return dao.getAll();
    }
}
