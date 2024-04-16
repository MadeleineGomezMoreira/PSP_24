package data.dao.impl;


import common.Constants;
import data.connection.DBConnectionPool;
import data.dao.mappers.BusStopMapper;
import domain.exception.ConnectionFailedException;
import domain.exception.NotFoundException;
import domain.model.BusStop;
import jakarta.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DaoStopsImpl implements data.dao.DaoStops {

    private final DBConnectionPool pool;

    @Inject
    public DaoStopsImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    //get all bus stops from a specific line
    @Override
    public List<BusStop> getAll(BusStop busStop) {
        int busLineId = busStop.getId();
        List<BusStop> busStops;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            busStops = jdbcTemplate.query(QueryStrings.GET_ALL_STOPS_IN_A_LINE, new BusStopMapper(), busLineId);
            if (busStops.isEmpty()) {
                throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_EMPTY_DATA_SOURCE);
            }
        } catch (DataAccessException e) {
            throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
        }
        return busStops;
    }

    //get all bus stops
    @Override
    public List<BusStop> getAll() {
        List<BusStop> busStops;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            busStops = jdbcTemplate.query(QueryStrings.GET_ALL_STOPS, new BusStopMapper());
            if (busStops.isEmpty()) {
                throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_EMPTY_DATA_SOURCE);
            }
        } catch (DataAccessException e) {
            throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
        }
        return busStops;
    }
}
