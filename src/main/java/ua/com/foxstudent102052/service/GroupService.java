package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDto groupDto) throws ServiceException;

    GroupDto getGroupById(int groupId) throws ServiceException;

    List<GroupDto> getAllGroups() throws ServiceException;

    List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws ServiceException;

    GroupDto getGroupByName(String groupName) throws ServiceException;

    List<StudentDto> getStudentsByGroup(int groupId) throws ServiceException;
}
