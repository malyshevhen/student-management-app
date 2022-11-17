package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDto groupDto);

    GroupDto getGroupById(int groupId);

    List<GroupDto> getAllGroups();

    List<GroupDto> getGroupsSmallerThen(int numberOfStudents);

    GroupDto getGroupByName(String groupName);

    List<StudentDto> getStudentsByGroup(int groupId);
}
