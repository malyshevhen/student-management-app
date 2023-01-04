package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.mapper.GroupModelMapper;
import ua.com.foxstudent102052.model.mapper.StudentModelMapper;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao;

    @Override
    public void addGroup(GroupDto groupDto) throws ServiceException {
        try {
            groupDao.addGroup(GroupModelMapper.toGroup(groupDto));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public GroupDto getGroup(int groupId) throws ServiceException {
        try {
            return groupDao.getGroup(groupId)
                .map(GroupModelMapper::toGroupDto)
                .orElseThrow();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public GroupDto getGroup(String groupName) throws ServiceException {
        try {
            return groupDao.getGroup(groupName)
                .map(GroupModelMapper::toGroupDto)
                .orElseThrow();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<GroupDto> getGroups() throws ServiceException {
        try {
            var groupDtoList = groupDao.getGroups()
                .stream()
                .map(GroupModelMapper::toGroupDto)
                .toList();

            if (groupDtoList.isEmpty()) {
                throw new NoSuchElementException("There are no groups in the database");
            } else {
                return groupDtoList;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<GroupDto> getGroupsLessThen(int numberOfStudents) throws ServiceException {
        try {
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
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<StudentDto> getStudentsByGroup(int groupId) throws ServiceException {
        try {
            var group = groupDao.getGroup(groupId).orElseThrow();
            var studentDtoList = groupDao.getStudents(groupId)
                .stream()
                .map(StudentModelMapper::toStudentDto)
                .toList();
            studentDtoList.forEach(studentDto -> studentDto.setGroup(GroupModelMapper.toGroupDto(group)));

            if (studentDtoList.isEmpty()) {
                throw new NoSuchElementException(String.format("There are no students in group with id %d", groupId));
            } else {
                return studentDtoList;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
