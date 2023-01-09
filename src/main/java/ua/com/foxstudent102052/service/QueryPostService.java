package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;

public class QueryPostService {
    private final PostDAO postDAO;

    public QueryPostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    public void executeQuery(String query) throws DAOException {
        postDAO.doPost(query);
    }
}
