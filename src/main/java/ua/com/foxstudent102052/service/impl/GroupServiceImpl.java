package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.mapper.GroupModelMapper;
import ua.com.foxstudent102052.model.mapper.StudentModelMapper;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    public static final String GROUP_DOES_NOT_EXIST = "This group does not exist in DB";
    private final GroupDao groupDao;

    @Override
    public void addGroup(GroupDto groupDto) throws DAOException {
        if (groupDao.getGroup(groupDto.getName()).isPresent()) {
            throw new ElementAlreadyExistException("Group is already exist!");
        } else {
            groupDao.addGroup(GroupModelMapper.toGroup(groupDto));
        }
    }

    @Override
    public GroupDto getGroup(int groupId) throws DAOException {
        return groupDao.getGroup(groupId)
            .map(GroupModelMapper::toGroupDto)
            .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
    }

    @Override
    public GroupDto getGroup(String groupName) throws DAOException {
        return groupDao.getGroup(groupName)
            .map(GroupModelMapper::toGroupDto)
            .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
    }

    @Override
    public List<GroupDto> getGroups() throws DAOException {
        var groupDtoList = groupDao.getGroups()
            .stream()
            .map(GroupModelMapper::toGroupDto)
            .toList();

        if (groupDtoList.isEmpty()) {
            throw new NoSuchElementException("There are no groups in the database");
        } else {
            return groupDtoList;
        }
    }

    @Override
    public List<GroupDto> getGroupsLessThen(int numberOfStudents) throws DAOException {
        var groupList = groupDao.getGroupsLessThen(numberOfStudents)
            .stream()
            .map(GroupModelMapper::toGroupDto)
            .toList();

        if (groupList.isEmpty()) {
            throw new NoSuchElementException(
                String.format("There are no groups with number of students less then %d", numberOfStudents));
        } else {
            return groupList;
        }
    }

    @Override
    public List<StudentDto> getStudentsByGroup(int groupId) throws DAOException {
        var group = groupDao.getGroup(groupId)
            .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
        var studentDtoList = groupDao.getStudents(groupId)
            .stream()
            .map(StudentModelMapper::toStudentDto)
            .peek(studentDto -> studentDto.setGroup(GroupModelMapper.toGroupDto(group)))
            .toList();

        if (studentDtoList.isEmpty()) {
            throw new NoSuchElementException(String.format("There are no students in group with id %d", groupId));
        } else {
            return studentDtoList;
        }
    }
}
