package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.dao.exceptions.DAOException;

public interface PostDAO {
    void doPost(String query) throws DAOException;
}
