package com.example.server.domain.usecases.driver;

import com.example.server.data.dao.impl.DaoDriversImpl;
import com.example.server.domain.model.BusDriver;
import jakarta.inject.Inject;

import java.util.List;

public class GetAllDrivers {

    private final DaoDriversImpl dao;

    @Inject
    public GetAllDrivers(DaoDriversImpl dao) {
        this.dao = dao;
    }

    public List<BusDriver> getAll() {
        return dao.getAll();
    }
}
