package data.dao;

import domain.model.BusLine;

import java.util.List;

public interface DaoLines {
    List<BusLine> getAll();

    List<BusLine> getAll(BusLine line);

    BusLine get(BusLine line);
}
