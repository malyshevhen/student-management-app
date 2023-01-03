package ua.com.foxstudent102052.dao.datasource.impl;

import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2CustomDataSource implements CustomDataSource {
    private static final String JDBC_URL = "jdbc:h2:file:C:/Users/t/IdeaProjects/versionTwo/student-management-app/db_test";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "sa";

    private static H2CustomDataSource instance;

    private H2CustomDataSource() {
    }

    public static H2CustomDataSource getInstance() {
        if (instance == null) {
            instance = new H2CustomDataSource();
        }

        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
    }
}
