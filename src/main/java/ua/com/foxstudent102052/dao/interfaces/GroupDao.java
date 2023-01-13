package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.model.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    void addGroup(Group group);

    List<Group> getAll();

    Optional<Group> getGroupById(int groupId);

    Optional<Group> getGroupByName(String groupName);

    List<Group> getGroupsLessThen(int numberOfStudents);
}
