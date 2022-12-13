package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.dto.GroupDto;
import ua.com.foxstudent102052.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ServiceException;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDto groupDto) throws ServiceException;

    GroupDto getGroup(int groupId) throws ServiceException;

    List<GroupDto> getGroups() throws ServiceException;

    List<GroupDto> getGroupsLessThen(int numberOfStudents) throws ServiceException;

    GroupDto getGroup(String groupName) throws ServiceException;

    List<StudentDto> getStudentsByGroup(int groupId) throws ServiceException;

    Boolean ifExist(String groupName) throws ServiceException;
}
