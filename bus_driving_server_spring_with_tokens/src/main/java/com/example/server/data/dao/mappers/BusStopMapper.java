package com.example.server.data.dao.mappers;

import com.example.server.common.DbConstants;
import com.example.server.domain.model.BusStop;
import com.example.server.domain.model.Point;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusStopMapper implements RowMapper<BusStop> {

    @Override
    public BusStop mapRow(ResultSet rs, int rowNum) throws SQLException {
        BusStop bs = new BusStop();
        bs.setId(rs.getInt(DbConstants.STOP_ID));
        bs.setName(rs.getString(DbConstants.STOP_NAME));
        double xCoordinate = rs.getDouble("x_coordinate");
        double yCoordinate = rs.getDouble("y_coordinate");
        bs.setLocation(new Point(xCoordinate, yCoordinate));
        return bs;
    }

    //myResultSet.getDouble("x_coordinate");
    //myResultSet.getDouble("y_coordinate");

//    @Override
//    public BusStop mapRow(ResultSet rs, int rowNum) throws SQLException {
//        BusStop bs = new BusStop();
//        bs.setId(rs.getInt(DbConstants.STOP_ID));
//        bs.setName(rs.getString(DbConstants.STOP_NAME));
//        Point location = (Point) rs.getObject(DbConstants.STOP_COORDINATE);
//        bs.setLocation(location);
//        return bs;
//    }
}

