package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private GroupDao groupDao;
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        groupDao = mock(GroupDao.class);
        groupService = new GroupServiceImpl(groupDao);
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

    @Test
    void MethodGetStudentsByGroup_ShouldReturnStudentsFromDb() throws DAOException, ElementAlreadyExistException {
        // given
        var studentList = List.of(
            Student.builder().groupId(1).firstName("SomeName1").lastName("SomeLastName1").build(),
            Student.builder().groupId(1).firstName("SomeName2").lastName("SomeLastName2").build(),
            Student.builder().groupId(1).firstName("SomeName3").lastName("SomeLastName3").build());

        int groupId = 1;
        var group = Group.builder().id(groupId).name("SomeGroup").build();
        var groupDto = modelMapper.map(group, GroupDto.class);

        var expected = studentList.stream()
            .map(student1 -> modelMapper.map(student1, StudentDto.class))
            .toList();
        expected.forEach(student -> student.setGroup(groupDto));

        // when
        when(groupDao.getGroup(groupId)).thenReturn(Optional.of(group));
        when(groupDao.getStudents(groupId)).thenReturn(studentList);

        var actual = groupService.getStudentsByGroup(groupId);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByGroupShould_TrowException_WhenRepositoryExceptionIsThrown() throws DAOException {
        doThrow(DAOException.class).when(groupDao).getStudents(1);

        assertThrows(NoSuchElementException.class, () -> groupService.getStudentsByGroup(1), "Students weren`t received");
    }
}
