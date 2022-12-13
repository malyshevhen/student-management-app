package ua.com.foxstudent102052.repository.interfaces;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;

import java.util.List;

public interface GroupRepository {
    void addGroup(Group group) throws RepositoryException;

    List<Group> getAllGroups() throws RepositoryException;

    Group getGroup(int groupId) throws RepositoryException;

    List<Group> getGroupsLessThen(int numberOfStudents) throws RepositoryException;

    Group getGroup(String groupName) throws RepositoryException;

    List<Student> getStudents(int groupId) throws RepositoryException;

    Group getLastGroup() throws RepositoryException;

    Boolean ifExist(String groupName) throws RepositoryException;
}
