package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    void addGroup(Group group);

    List<Group> getGroups();

    Optional<Group> getGroup(int groupId);

    Optional<Group> getGroup(String groupName);

    List<Group> getGroupsLessThen(int numberOfStudents);

    List<Student> getStudents(int groupId);
}
