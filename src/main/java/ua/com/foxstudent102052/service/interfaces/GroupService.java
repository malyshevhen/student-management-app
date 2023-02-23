package ua.com.foxstudent102052.service.interfaces;

import java.util.List;

import ua.com.foxstudent102052.model.dto.GroupDto;

public interface GroupService {
    void addGroup(GroupDto groupDto);

    GroupDto getGroupById(Long groupId);

    List<GroupDto> getAll();

    List<GroupDto> getGroupsLessThen(Long numberOfStudents);

    GroupDto getGroupByName(String groupName);
}
