package data.dao.impl;

import common.Constants;
import data.connection.DBConnectionPool;
import data.dao.DaoCredentials;
import data.dao.mappers.CredentialMapper;
import domain.exception.ActivationFailedException;
import domain.exception.AuthenticationFailedException;
import domain.exception.ConnectionFailedException;
import domain.model.DriverCredential;
import jakarta.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class DaoCredentialsImpl implements DaoCredentials {

    private final DBConnectionPool pool;

    @Inject
    public DaoCredentialsImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public DriverCredential getCredential(DriverCredential credential) {
        DriverCredential result = null;
        String username = credential.getUsername();
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<DriverCredential> credentials = jdbcTemplate.query(QueryStrings.GET_CREDENTIAL_BY_USERNAME_WITH_ROLE, new CredentialMapper(), username);
            if (credentials.isEmpty()) {
                throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_USERNAME_ERROR);
            }else {
                result = credentials.get(0);
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            }
        }
        return result;
    }

    @Override
    public DriverCredential getCredentialById(DriverCredential credential) {
        DriverCredential result = null;
        int credentialId = credential.getId();
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<DriverCredential> credentials = jdbcTemplate.query(QueryStrings.GET_CREDENTIAL_BY_ID_WITH_ROLE, new CredentialMapper(), credentialId);
            if (credentials.isEmpty()) {
                throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_USERNAME_ERROR);
            }else {
                result = credentials.get(0);
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            }
        }
        return result;
    }

    @Override
    public DriverCredential getCredentialByEmail(DriverCredential credential) {
        DriverCredential result = null;
        String email = credential.getEmail();
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<DriverCredential> credentials = jdbcTemplate.query(QueryStrings.GET_CREDENTIAL_BY_EMAIL_WITH_ROLE, new CredentialMapper(), email);
            if (credentials.isEmpty()) {
                throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_EMAIL_ERROR);
            } else {
                result = credentials.get(0);
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            }
        }
        return result;
    }

    @Override
    public boolean update(DriverCredential credential) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            int rowsUpdated = jdbcTemplate.update(QueryStrings.UPDATE_CREDENTIAL, credential.getUsername(), credential.getPassword(), credential.getEmail(), credential.isActivated(), credential.getActivationDate(), credential.getActivationCode(), credential.getRole().getRoleId(), credential.getId());
            if (rowsUpdated == 0) {
                throw new ActivationFailedException(Constants.UPDATE_FAILED_CREDENTIAL);
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ActivationFailedException(Constants.UPDATE_FAILED_CREDENTIAL);
            } else {
                throw new ActivationFailedException(Constants.INTERNAL_SERVER_ERROR);
            }
        }
        return true;
    }

}
