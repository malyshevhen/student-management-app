package ua.com.foxstudent102052.dao.datasource.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class PooledDataSource implements CustomDataSource {
    private static final String PROPERTIES_FILE_NAME = "datasource.properties";
    private static final Properties properties = new Properties();
    private static final FileUtils fileUtils = new FileUtils();
    private static final HikariConfig config;
    private static final HikariDataSource hikariDataSource;

    private static PooledDataSource instance;

    static {
        loadPropertiesFromResources();
        config = new HikariConfig(properties);
        hikariDataSource = new HikariDataSource(config);
    }

    private PooledDataSource() {
    }

    public static PooledDataSource getInstance() {
        if (instance == null) {
            instance = new PooledDataSource();
        }

        return instance;
    }

    private static void loadPropertiesFromResources() {
        try {
            properties.load(fileUtils.getFileFromResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found: " + PROPERTIES_FILE_NAME);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
