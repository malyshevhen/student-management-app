package ua.com.foxstudent102052.dao.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostDAOImpl implements PostDAO {
    private static final String QUERY_EXEC_SUCCESSFUL = "Query: '{}' executed successfully";
    private static final String CONNECTION_SUCCESSFUL = "Connection to DB successful!";

    private final DataSource dataSource;

    @Override
    public void doPost(String query) {
        try (var connection = dataSource.getConnection();
                var statement = connection.createStatement()) {

            log.info(CONNECTION_SUCCESSFUL);
            statement.executeUpdate(query);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }
}
