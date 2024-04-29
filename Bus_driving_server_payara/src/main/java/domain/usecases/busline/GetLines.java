package domain.usecases.busline;

import data.dao.DaoLines;
import domain.model.BusLine;
import jakarta.inject.Inject;

import java.util.List;

public class GetLines {

    private final DaoLines dao;

    @Inject
    public GetLines(DaoLines dao) {
        this.dao = dao;
    }

    public List<BusLine> getAll() {
        return dao.getAll();
    }
}
