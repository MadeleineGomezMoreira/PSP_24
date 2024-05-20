package com.example.server.data.dao.mappers;

import com.example.server.common.DbConstants;
import com.example.server.domain.model.BusLine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusLineMapper implements RowMapper<BusLine> {

    @Override
    public BusLine mapRow(ResultSet rs, int rowNum) throws SQLException {
        BusLine bl = new BusLine();
        bl.setId(rs.getInt(DbConstants.LINE_ID));
        bl.setLineStart(rs.getString(DbConstants.LINE_START));
        bl.setLineEnd(rs.getString(DbConstants.LINE_END));
        return bl;
    }
}
