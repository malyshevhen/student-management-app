package ua.com.foxstudent102052.datasource.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.com.foxstudent102052.datasource.interfaces.CustomDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresPooledDataSource implements CustomDataSource {
    private static final String PROPERTIES_PATH = "src/main/resources/datasource.properties";
    private static final HikariConfig config;
    private static final HikariDataSource hikariDataSource;

    private static PostgresPooledDataSource instance;

    private PostgresPooledDataSource() {
    }

    static {
        config = new HikariConfig(PROPERTIES_PATH);
        hikariDataSource = new HikariDataSource(config);
    }

    public static PostgresPooledDataSource getInstance() {
        if (instance == null) {
            instance = new PostgresPooledDataSource();
        }

        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
