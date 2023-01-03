package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.model.mapper.GroupModelMapper;
import ua.com.foxstudent102052.model.mapper.StudentModelMapper;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {
    private GroupDao groupDao;
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        groupDao = mock(GroupDao.class);
        groupService = new GroupServiceImpl(groupDao);
    }

    @Test
    void MethodAddGroup_ShouldPassGroupToRepository() throws DAOException, ServiceException {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupModelMapper.toGroupDto(group);

        // when
        groupService.addGroup(groupDto);

        // then
        verify(groupDao).addGroup(group);
    }

    @Test
    void MethodAddGroup_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws DAOException {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupModelMapper.toGroupDto(group);

        // when
        doThrow(DAOException.class).when(groupDao).addGroup(group);

        // then
        assertThrows(ServiceException.class, () -> groupService.addGroup(groupDto), "Group wasn`t added");
    }

    @Test
    void MethodGetAllGroups_ShouldReturnAllGroupsFromDb() throws DAOException, ServiceException {
        // given
        var groups = List.of(
            Group.builder().groupName("SomeGroup1").build(),
            Group.builder().groupName("SomeGroup2").build(),
            Group.builder().groupName("SomeGroup3").build());

        var expected = groups.stream()
            .map(GroupModelMapper::toGroupDto)
            .toList();

        // when
        when(groupDao.getGroups()).thenReturn(groups);

        var actual = groupService.getGroups();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllGroups_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws DAOException {
        // when
        doThrow(DAOException.class).when(groupDao).getGroups();

        // then
        assertThrows(ServiceException.class, () -> groupService.getGroups(), "Groups weren`t received");
    }

    @Test
    void MethodGetGroupById_ShouldReturnGroupFromDb() throws DAOException, ServiceException {
        var optionalGroup = Optional.of(Group.builder().groupName("SomeGroup").build());
        when(groupDao.getGroup(1)).thenReturn(optionalGroup);

        var expected = GroupDto.builder().name("SomeGroup").studentList(List.of()).build();

        var actual = groupService.getGroup(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupById_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws DAOException {
        doThrow(DAOException.class).when(groupDao).getGroup(1);

        assertThrows(ServiceException.class, () -> groupService.getGroup(1), "Group wasn`t received");
    }

    @Test
    void MethodGetGroupByName_ShouldReturnGroupFromDb() throws DAOException, ServiceException {
        var optionalGroup = Optional.of(Group.builder().groupName("SomeGroup").build());
        when(groupDao.getGroup("SomeGroup")).thenReturn(optionalGroup);

        var expected = GroupDto.builder().name("SomeGroup").studentList(List.of()).build();

        var actual = groupService.getGroup("SomeGroup");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByName_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws DAOException {
        doThrow(DAOException.class).when(groupDao).getGroup("SomeGroup");

        assertThrows(ServiceException.class, () -> groupService.getGroup("SomeGroup"), "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsSmallerThen_ShouldReturnGroupsFromDb() throws DAOException, ServiceException {
        // given
        var groups = List.of(
            Group.builder().groupName("SomeGroup1").build(),
            Group.builder().groupName("SomeGroup2").build(),
            Group.builder().groupName("SomeGroup3").build());

        var expected = groups.stream()
            .map(GroupModelMapper::toGroupDto)
            .toList();

        // when
        when(groupDao.getGroupsLessThen(3)).thenReturn(groups);

        var actual = groupService.getGroupsLessThen(3);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsSmallerThen_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws DAOException {
        // when
        doThrow(DAOException.class).when(groupDao).getGroupsLessThen(3);

        // then
        assertThrows(ServiceException.class, () -> groupService.getGroupsLessThen(3), "Groups weren`t received");
    }

    @Test
    void MethodGetStudentsByGroup_ShouldReturnStudentsFromDb() throws DAOException, ServiceException {
        // given
        var studentList = List.of(
            Student.builder().groupId(1).firstName("SomeName1").lastName("SomeLastName1").build(),
            Student.builder().groupId(1).firstName("SomeName2").lastName("SomeLastName2").build(),
            Student.builder().groupId(1).firstName("SomeName3").lastName("SomeLastName3").build());

        int groupId = 1;
        var group = Group.builder().groupId(groupId).groupName("SomeGroup").build();
        var groupDto = GroupModelMapper.toGroupDto(group);

        var expected = studentList.stream()
            .map(StudentModelMapper::toStudentDto)
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
