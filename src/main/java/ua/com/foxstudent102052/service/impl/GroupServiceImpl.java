package ua.com.foxstudent102052.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.GroupRepository;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupServiceImpl implements GroupService {
    public static final String GROUP_DOES_NOT_EXIST = "This group does not exist in DB";

    private final GroupRepository groupDao;
    private final ModelMapper modelMapper;

    @Override
    public void addGroup(GroupDto groupDto) throws DataAccessException {
        if (groupDao.findByGroupName(groupDto.getGroupName()).isPresent()) {
            throw new ElementAlreadyExistException("Group is already exist!");
        } else {
            groupDao.save(modelMapper.map(groupDto, Group.class));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GroupDto getGroupById(int groupId) throws DataAccessException {
        return groupDao.findById(groupId)
                .map(group -> modelMapper.map(group, GroupDto.class))
                .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    @Override
    public GroupDto getGroupByName(String groupName) throws DataAccessException {
        return groupDao.findByGroupName(groupName)
                .map(group -> modelMapper.map(group, GroupDto.class))
                .orElseThrow(() -> new NoSuchElementException(GROUP_DOES_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupDto> getAll() throws DataAccessException {
        var groupDtoList = groupDao.findAll()
                .stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .toList();

        if (groupDtoList.isEmpty()) {
            throw new NoSuchElementException("There are no groups in the database");
        } else {
            return groupDtoList;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupDto> getGroupsLessThen(int numberOfStudents) throws DataAccessException {
        var groupList = groupDao.findByStudentsCount(numberOfStudents)
                .stream()
                .map(g -> modelMapper.map(g, GroupDto.class))
                .toList();

        if (groupList.isEmpty()) {
            throw new NoSuchElementException(
                    String.format("There are no groups with number of students less then %d", numberOfStudents));
        } else {
            return groupList;
        }
    }
}
