package data.dao.impl;

import common.Constants;
import common.DbConstants;
import data.connection.DBConnectionPool;
import data.dao.DaoDrivers;
import data.dao.mappers.BusDriverMapper;
import domain.errors.BusError;
import domain.exception.*;
import domain.model.BusDriver;
import domain.model.DriverCredential;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DaoDriversImpl implements DaoDrivers {

    private final DBConnectionPool pool;

    @Inject
    public DaoDriversImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public List<BusDriver> getAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
        List<BusDriver> drivers = jdbcTemplate.query(QueryStrings.GET_ALL_DRIVERS_WITH_LINES, new BusDriverMapper());
        if (drivers.isEmpty()) {
            throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_EMPTY_DATA_SOURCE);
        }
        drivers = drivers.stream()
                .filter(driver -> driver.getId() != -1)
                .collect(Collectors.toCollection(ArrayList::new));
        return drivers;
    }

    @Override
    public BusDriver get(BusDriver driver) {
        BusDriver result = null;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            int id = driver.getId();
            List<BusDriver> drivers = jdbcTemplate.query(QueryStrings.GET_DRIVER_BY_ID_WITH_BUS_LINE, new BusDriverMapper(), id);
            if (drivers.isEmpty()) {
                throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND);
            }
            result = drivers.get(0);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND);
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            }
        }
        return result;
    }

    @Override
    public BusDriver update(BusDriver driver) {
        int affectedRows;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            affectedRows = jdbcTemplate.update(
                    QueryStrings.UPDATE_DRIVER,
                    driver.getFirstName(),
                    driver.getLastName(),
                    driver.getPhone(),
                    driver.getAssignedLine().getId(),
                    driver.getId());

            if (affectedRows != 0) {
                return driver;
            } else {
                throw new UpdateFailedException(Constants.NO_ROWS_AFFECTED);
            }
        } catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            } else {
                throw new GeneralDatabaseException(Constants.GENERAL_DATABASE_ERROR);
            }
        }
    }

    @Override
    public boolean delete(BusDriver driver) {
        int affectedRows;
        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            jdbcTemplate.update(QueryStrings.DELETE_DRIVER, driver.getId());
            affectedRows = jdbcTemplate.update(QueryStrings.DELETE_CREDENTIAL, driver.getId());
            if (affectedRows == 0) {
                transactionManager.rollback(status);
                throw new UpdateFailedException(Constants.NO_ROWS_AFFECTED);
            } else {
                transactionManager.commit(status);
            }
        } catch (DataAccessException e) {
            transactionManager.rollback(status);
            if (e.getCause() instanceof SQLException) {
                throw new ConnectionFailedException(Constants.CONNECTION_TO_DATABASE_FAILED);
            } else {
                throw new GeneralDatabaseException(Constants.GENERAL_DATABASE_ERROR);
            }
        }
        return true;
    }

    @Override
    public BusDriver save(BusDriver driver) {
        int affectedRows;
        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);

        Map<String, Object> params;

        SimpleJdbcInsert insert = new SimpleJdbcInsert(pool.getDataSource())
                .withTableName(DbConstants.DRIVER_CREDENTIALS_TABLE)
                .usingColumns(DbConstants.USERNAME, DbConstants.PASSWORD, DbConstants.EMAIL, DbConstants.ACTIVATED, DbConstants.ACTIVATION_DATE, DbConstants.ACTIVATION_CODE)
                .usingGeneratedKeyColumns(DbConstants.CREDENTIAL_ID);

        params = new HashMap<>();

        DriverCredential credential = driver.getCredential();

        params.put(DbConstants.USERNAME, credential.getUsername());
        params.put(DbConstants.PASSWORD, credential.getPassword());
        params.put(DbConstants.EMAIL, credential.getEmail());
        params.put(DbConstants.ACTIVATED, credential.isActivated());
        params.put(DbConstants.ACTIVATION_DATE, credential.getActivationDate());
        params.put(DbConstants.ACTIVATION_CODE, credential.getActivationCode());

        int generatedKey;
        Either<BusError, Integer> credentialInsertResult = insertCredential(status, transactionManager, insert, params);

        if (credentialInsertResult.isRight()) {
            generatedKey = credentialInsertResult.get();
        } else {
            transactionManager.rollback(status);
            throw new UsernameAlreadyExistsException(Constants.UNIQUE_FIELD_CONSTRAINT_ERROR);
        }

        if (generatedKey == 0) {
            transactionManager.rollback(status);
            throw new InsertFailedException(Constants.NO_GENERATED_KEY);
        } else {

            insert = new SimpleJdbcInsert(pool.getDataSource())
                    .withTableName(DbConstants.BUS_DRIVER_TABLE)
                    .usingColumns(DbConstants.DRIVER_ID, DbConstants.FIRST_NAME, DbConstants.LAST_NAME, DbConstants.PHONE, DbConstants.ASSIGNED_LINE);

            params = new HashMap<>();

            params.put(DbConstants.DRIVER_ID, generatedKey);
            params.put(DbConstants.FIRST_NAME, driver.getFirstName());
            params.put(DbConstants.LAST_NAME, driver.getLastName());
            params.put(DbConstants.PHONE, driver.getPhone());

            affectedRows = insert.execute(params);
            if (affectedRows == 0) {
                transactionManager.rollback(status);
                throw new InsertFailedException(Constants.NO_ROWS_AFFECTED);
            } else {
                transactionManager.commit(status);
                return driver;
            }
        }
    }

    private Either<BusError, Integer> insertCredential(TransactionStatus status, DataSourceTransactionManager transactionManager, SimpleJdbcInsert insert, Map<String, Object> params) {
        int generatedKey;
        try {
            generatedKey = insert.executeAndReturnKey(params).intValue();
            return Either.right(generatedKey);
        } catch (DuplicateKeyException e) {
            transactionManager.rollback(status);
            return Either.left(new BusError(Constants.UNIQUE_FIELD_CONSTRAINT_ERROR));
        }
    }
}
