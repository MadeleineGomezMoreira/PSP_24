package data.dao.impl;

import common.Constants;
import data.connection.DBConnectionPool;
import data.dao.DaoCredentials;
import data.dao.mappers.CredentialMapper;
import domain.exception.AccountNotActivatedException;
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
            } else if (!credentials.get(0).isActivated()) {
                throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
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
    public boolean activateCredential(DriverCredential credential) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            Boolean isSuitable = jdbcTemplate.queryForObject(QueryStrings.GET_ACTIVATION_DATE_SUITABILITY, Boolean.class, credential.getActivationDate(), credential.getActivationDate(), credential.getActivationCode(), credential.getEmail());
            if (isSuitable == null) {
                throw new ActivationFailedException(Constants.ACTIVATION_FAILED);
            } else if (!isSuitable) {
                throw new ActivationFailedException(Constants.ACTIVATION_LINK_EXPIRED);
            } else {
                jdbcTemplate = new JdbcTemplate(pool.getDataSource());
                jdbcTemplate.update(QueryStrings.ACTIVATE_CREDENTIAL, credential.getActivationCode(), credential.getEmail());
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ActivationFailedException(Constants.ACTIVATION_FAILED);
            }
        }
        return true;
    }

    @Override
    public boolean verifyCredential(DriverCredential credential) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<DriverCredential> credentials = jdbcTemplate.query(QueryStrings.VERIFY_CREDENTIAL, new CredentialMapper(), credential.getId(), credential.getRole().getRoleId());

            if (credentials.isEmpty()) {
                throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED);
            } else {
                DriverCredential firstCredential = credentials.get(0);

                if(firstCredential.getRole().getRoleId() != credential.getRole().getRoleId()) {
                    throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED);
                }

                if (firstCredential != null && !firstCredential.isActivated()) {
                    throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
                }

                return true;
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            }
        }
        return false;
    }



}
