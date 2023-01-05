package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.impl.PostDAOImpl;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;

public class QueryPostService {
    private final PostDAO daoFactory;

    public QueryPostService(CustomDataSource customDataSource) {
        daoFactory = new PostDAOImpl(customDataSource);
    }

    public void executeQuery(String query) throws DAOException {
        daoFactory.doPost(query);
    }
}
