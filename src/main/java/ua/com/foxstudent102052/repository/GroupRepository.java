package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface GroupRepository {
    void addGroup(Group group);

    void removeGroupById(int groupId);

    void updateGroupById(Group group);

    List<Group> getAllGroups();

    Group getGroupById(int groupId);

    List<Group> getGroupsSmallerThen(int numberOfStudents);

    Group getGroupByName(String groupName);

    List<Student> getStudentsByGroup(int groupId);
}
