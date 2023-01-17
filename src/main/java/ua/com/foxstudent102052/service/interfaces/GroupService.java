package ua.com.foxstudent102052.service.interfaces;

import java.util.List;

import ua.com.foxstudent102052.model.dto.GroupDto;

public interface GroupService {
    void addGroup(GroupDto groupDto);

    GroupDto getGroup(int groupId);

    List<GroupDto> getGroups();

    List<GroupDto> getGroupsLessThen(int numberOfStudents);

    GroupDto getGroup(String groupName);
}
