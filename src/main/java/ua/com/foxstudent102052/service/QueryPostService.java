package ua.com.foxstudent102052.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QueryPostService {
    private final PostDAO postDAO;

    public void executeQuery(String query) throws DAOException {
        postDAO.doPost(query);
    }
}
