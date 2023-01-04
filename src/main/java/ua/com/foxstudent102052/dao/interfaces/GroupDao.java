package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    void addGroup(Group group) throws DAOException;

    List<Group> getGroups() throws DAOException;

    Optional<Group> getGroup(int groupId) throws DAOException;

    Optional<Group> getGroup(String groupName) throws DAOException;

    List<Group> getGroupsLessThen(int numberOfStudents) throws DAOException;

    List<Student> getStudents(int groupId) throws DAOException;
}
