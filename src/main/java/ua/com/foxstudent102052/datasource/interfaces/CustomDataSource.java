package ua.com.foxstudent102052.datasource.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface CustomDataSource {
    Connection getConnection() throws SQLException;
}
