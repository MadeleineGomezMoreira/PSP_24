package com.example.server.data.dao;

import com.example.server.domain.model.BusLine;

import java.util.List;

public interface DaoLines {
    List<BusLine> getAll();

    List<BusLine> getAll(BusLine line);

    BusLine get(BusLine line);
}
