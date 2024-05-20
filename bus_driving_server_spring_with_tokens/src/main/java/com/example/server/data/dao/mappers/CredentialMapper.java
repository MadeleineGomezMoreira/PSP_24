package com.example.server.data.dao.mappers;

import com.example.server.domain.model.AccountRole;
import com.example.server.domain.model.DriverCredential;
import org.springframework.jdbc.core.RowMapper;

public class CredentialMapper implements RowMapper<DriverCredential> {

    @Override
    public DriverCredential mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        DriverCredential dc = new DriverCredential();
        dc.setId(rs.getInt("credential_id"));
        dc.setUsername(rs.getString("username"));
        dc.setPassword(rs.getString("password"));
        dc.setEmail(rs.getString("email"));
        dc.setActivated(rs.getBoolean("activated"));
        dc.setActivationDate(rs.getTimestamp("activation_date").toLocalDateTime());
        dc.setActivationCode(rs.getString("activation_code"));
        AccountRole ar = new AccountRole(
                rs.getInt("role_id"),
                rs.getString("role_name")
        );
        dc.setRole(ar);
        return dc;
    }

}
