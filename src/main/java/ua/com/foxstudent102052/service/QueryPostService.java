package ua.com.foxstudent102052.service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.impl.PostDAOImpl;
import ua.com.foxstudent102052.datasource.impl.PostgresPooledDataSource;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.service.exceptions.ServiceException;

@Slf4j
public class QueryPostService {
    private static final PostDAO daoFactory = new PostDAOImpl(PostgresPooledDataSource.getInstance());

    private QueryPostService() {
    }

    public static void executeQuery(String query) throws ServiceException {
        try {
            daoFactory.doPost(query);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
