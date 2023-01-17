package ua.com.foxstudent102052.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QueryPostService {
    private final PostDAO postDAO;

    public void executeQuery(String query) throws DataAccessException {
        postDAO.doPost(query);
    }
}
