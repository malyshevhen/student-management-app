package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface GroupService {
    void addGroup(Group group);

    void updateGroup(Group group);

    void removeGroup(int groupId);

    Group getGroupById(int groupId);

    List<Group> getAllGroups();

    List<Group> getGroupsSmallerThen(int numberOfStudents);

    Group getGroupByName(String groupName);

    List<Student> getStudentsByGroup(int groupId);
}
