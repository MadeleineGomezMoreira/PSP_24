package data.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.Constants;
import common.config.ConfigSettings;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@Singleton
public class DBConnectionPool {
    private final ConfigSettings configSettings;
    private final DataSource hikariDataSource;

    @Inject
    public DBConnectionPool(ConfigSettings configSettings) {
        this.configSettings = configSettings;
        hikariDataSource = getHikariPool();
    }

    private DataSource getHikariPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configSettings.getDbUrl());
        hikariConfig.setUsername(configSettings.getDbUser());
        hikariConfig.setPassword(configSettings.getDbPassword());
        hikariConfig.setDriverClassName(configSettings.getDbDriver());
        hikariConfig.setMaximumPoolSize(4);
        hikariConfig.addDataSourceProperty(Constants.CACHE_PREP_STMTS, true);
        hikariConfig.addDataSourceProperty(Constants.PREP_STMT_CACHE_SIZE, 250);
        hikariConfig.addDataSourceProperty(Constants.PREP_STMT_CACHE_SQL_LIMIT, 2048);
        return new HikariDataSource(hikariConfig);
    }

    public DataSource getDataSource() {
        return hikariDataSource;
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = hikariDataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return con;
    }

    @PreDestroy
    public void closePool() {
        ((HikariDataSource) hikariDataSource).close();
    }
}



