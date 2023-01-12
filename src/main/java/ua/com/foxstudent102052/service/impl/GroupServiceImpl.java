package ua.com.foxstudent102052.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupServiceImpl implements GroupService {
    public static final String GROUP_DOES_NOT_EXIST = "This group does not exist in DB";

    private final GroupDao groupDao;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public void addGroup(GroupDto groupDto) throws DAOException {
        if (groupDao.getGroup(groupDto.getName()).isPresent()) {
            throw new ElementAlreadyExistException("Group is already exist!");
        } else {
            groupDao.addGroup(modelMapper.map(groupDto, Group.class));
        }
    }

    @Override
    public GroupDto getGroup(int groupId) throws DAOException {
        return groupDao.getGroup(groupId)
            .map(group -> modelMapper.map(group, GroupDto.class))
            .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
    }

    @Override
    public GroupDto getGroup(String groupName) throws DAOException {
        return groupDao.getGroup(groupName)
            .map(group -> modelMapper.map(group, GroupDto.class))
            .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
    }

    @Override
    public List<GroupDto> getGroups() throws DAOException {
        var groupDtoList = groupDao.getGroups()
            .stream()
            .map(group -> modelMapper.map(group, GroupDto.class))
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
            .map(group -> modelMapper.map(group, GroupDto.class))
            .toList();

        if (groupList.isEmpty()) {
            throw new NoSuchElementException(
                String.format("There are no groups with number of students less then %d", numberOfStudents));
        } else {
            return groupList;
        }
    }
}
