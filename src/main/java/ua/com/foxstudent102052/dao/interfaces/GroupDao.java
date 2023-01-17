package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;
import java.util.Optional;

import ua.com.foxstudent102052.model.entity.Group;

public interface GroupDao {
    void addGroup(Group group);

    List<Group> getGroups();

    Optional<Group> getGroup(int groupId);

    Optional<Group> getGroup(String groupName);

    List<Group> getGroupsLessThen(int numberOfStudents);
}
