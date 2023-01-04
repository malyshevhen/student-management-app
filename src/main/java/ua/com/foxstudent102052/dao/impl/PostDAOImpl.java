package ua.com.foxstudent102052.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;

import java.sql.SQLException;

@Slf4j
public class PostDAOImpl implements ua.com.foxstudent102052.dao.interfaces.PostDAO {
    private static final String QUERY_EXEC_SUCCESSFUL = "Query: '{}' executed successfully";
    private static final String CONNECTION_SUCCESSFUL = "Connection to DB successful!";
    private static final String ERROR_MESSAGE = "Error while executing query: {}";

    private final CustomDataSource customDataSource;

    public PostDAOImpl(CustomDataSource customDataSource) {
        this.customDataSource = customDataSource;
    }

    @Override
    public void doPost(String query) throws DAOException {
        try (var connection = customDataSource.getConnection();
             var statement = connection.createStatement()) {

            log.info(CONNECTION_SUCCESSFUL);
            statement.executeUpdate(query);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }
}
