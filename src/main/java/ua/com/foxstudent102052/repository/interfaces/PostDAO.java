package ua.com.foxstudent102052.repository.interfaces;

import ua.com.foxstudent102052.repository.exceptions.DAOException;

public interface PostDAO {
    void doPost(String query) throws DAOException;
}
