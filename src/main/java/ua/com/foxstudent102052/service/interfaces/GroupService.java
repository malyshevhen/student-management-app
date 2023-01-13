package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.model.dto.GroupDto;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDto groupDto);

    GroupDto getGroupById(int groupId);

    List<GroupDto> getAll();

    List<GroupDto> getGroupsLessThen(int numberOfStudents);

    GroupDto getGroupByName(String groupName);
}
