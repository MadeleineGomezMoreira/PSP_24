package data.dao.impl;

import common.Constants;
import data.connection.DBConnectionPool;
import data.dao.DaoLines;
import data.dao.mappers.BusLineMapper;
import domain.exception.NotFoundException;
import domain.model.BusLine;
import jakarta.inject.Inject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DaoLinesImpl implements DaoLines {

    private final DBConnectionPool pool;

    @Inject
    public DaoLinesImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public List<BusLine> getAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
        List<BusLine> lines = jdbcTemplate.query(QueryStrings.GET_ALL_LINES, new BusLineMapper());
        if (lines.isEmpty()) {
            throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_EMPTY_DATA_SOURCE);
        }
        return lines;
    }

    @Override
    public BusLine get(BusLine line) {
        int lineId = line.getId();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
        List<BusLine> lines = jdbcTemplate.query(QueryStrings.GET_LINE_BY_ID, new BusLineMapper(), lineId);
        if (lines.isEmpty()) {
            throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_EMPTY_DATA_SOURCE);
        }
        return lines.get(0);
    }
}
