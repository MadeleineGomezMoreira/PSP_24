package data.dao.mappers;

import common.DbConstants;
import domain.model.BusDriver;
import domain.model.BusLine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusDriverMapper implements RowMapper<BusDriver> {

    @Override
    public BusDriver mapRow(ResultSet rs, int rowNum) throws SQLException {
        BusDriver bd = new BusDriver();
        bd.setId(rs.getInt(DbConstants.DRIVER_ID));
        bd.setFirstName(rs.getString(DbConstants.FIRST_NAME));
        bd.setLastName(rs.getString(DbConstants.LAST_NAME));
        bd.setPhone(rs.getString(DbConstants.PHONE));
        BusLine bl = new BusLine(
                rs.getInt(DbConstants.LINE_ID),
                rs.getString(DbConstants.LINE_START),
                rs.getString(DbConstants.LINE_END)
        );
        bd.setAssignedLine(bl);
        return bd;
    }
}

