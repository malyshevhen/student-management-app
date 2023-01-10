package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDto groupDto);

    GroupDto getGroup(int groupId);

    List<GroupDto> getGroups();

    List<GroupDto> getGroupsLessThen(int numberOfStudents);

    GroupDto getGroup(String groupName);

    List<StudentDto> getStudentsByGroup(int groupId);
}
