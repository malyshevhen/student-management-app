package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface GroupRepository {
    void addGroup(Group group) throws RepositoryException;

    List<Group> getAllGroups() throws RepositoryException;

    Group getGroupById(int groupId) throws RepositoryException;

    List<Group> getGroupsSmallerThen(int numberOfStudents) throws RepositoryException;

    Group getGroupByName(String groupName) throws RepositoryException;

    List<Student> getStudentsByGroup(int groupId) throws RepositoryException;

    Group getLastGroup() throws RepositoryException;

    Boolean ifExist(String groupName) throws RepositoryException;
}
