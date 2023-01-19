package ua.com.foxstudent102052.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

class GroupServiceImplTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private GroupDao groupDao;
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        groupDao = mock(GroupDao.class);
        groupService = new GroupServiceImpl(groupDao, modelMapper);
    }

    @Test
    void MethodAddGroup_ShouldPassGroupToRepository() throws DAOException, ElementAlreadyExistException {
        // given
        var group = Group.builder().name("SomeGroup").build();
        var groupDto = modelMapper.map(group, GroupDto.class);

        // when
        groupService.addGroup(groupDto);

        // then
        verify(groupDao).addGroup(group);
    }

    @Test
    void MethodGetAllGroups_ShouldReturnAllGroupsFromDb() throws DAOException, ElementAlreadyExistException {
        // given
        var groups = List.of(
                Group.builder().name("SomeGroup1").build(),
                Group.builder().name("SomeGroup2").build(),
                Group.builder().name("SomeGroup3").build());

        var expected = groups.stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .toList();

        // when
        when(groupDao.getGroups()).thenReturn(groups);

        var actual = groupService.getGroups();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupById_ShouldReturnGroupFromDb() throws DAOException, ElementAlreadyExistException {
        var optionalGroup = Optional.of(Group.builder().name("SomeGroup").build());
        when(groupDao.getGroup(1)).thenReturn(optionalGroup);

        var expected = optionalGroup.map(group -> modelMapper.map(group, GroupDto.class)).orElseThrow();

        var actual = groupService.getGroup(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByName_ShouldReturnGroupFromDb() throws DAOException, ElementAlreadyExistException {
        var optionalGroup = Optional.of(Group.builder().name("SomeGroup").build());
        when(groupDao.getGroup("SomeGroup")).thenReturn(optionalGroup);

        var expected = optionalGroup.map(group -> modelMapper.map(group, GroupDto.class)).orElseThrow();

        var actual = groupService.getGroup("SomeGroup");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsSmallerThen_ShouldReturnGroupsFromDb() throws DAOException, ElementAlreadyExistException {
        // given
        var groups = List.of(
                Group.builder().name("SomeGroup1").build(),
                Group.builder().name("SomeGroup2").build(),
                Group.builder().name("SomeGroup3").build());

        var expected = groups.stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .toList();

        // when
        when(groupDao.getGroupsLessThen(3)).thenReturn(groups);

        var actual = groupService.getGroupsLessThen(3);

        // then
        assertEquals(expected, actual);
    }
}
