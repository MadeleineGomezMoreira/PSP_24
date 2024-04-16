package data.dao.mappers;

import common.DbConstants;
import domain.model.BusLine;
import domain.model.BusStop;
import domain.model.Point;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusStopMapper implements RowMapper<BusStop> {

    @Override
    public BusStop mapRow(ResultSet rs, int rowNum) throws SQLException {
        BusStop bs = new BusStop();
        bs.setId(rs.getInt(DbConstants.STOP_ID));
        bs.setName(rs.getString(DbConstants.STOP_NAME));
        byte[] coordinateBytes = rs.getBytes(DbConstants.STOP_COORDINATE);
        bs.setLocation(Point.fromByteArray(coordinateBytes));
        return bs;
    }
}
