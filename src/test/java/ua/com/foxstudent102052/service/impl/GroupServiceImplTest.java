package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.mapper.GroupMapper;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.dto.GroupDto;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.dto.StudentDto;
import ua.com.foxstudent102052.repository.interfaces.GroupRepository;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {
    private GroupRepository groupRepository;
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        groupRepository = mock(GroupRepository.class);
        groupService = new GroupServiceImpl(groupRepository);
    }

    @Test
    void MethodAddGroup_ShouldPassGroupToRepository() throws RepositoryException, ServiceException {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupDto.builder().name("SomeGroup").build();

        // when
        doThrow(RepositoryException.class).when(groupRepository).getGroup(anyString());
        groupService.addGroup(groupDto);

        // then
        verify(groupRepository).addGroup(group);
    }

    @Test
    void MethodAddGroup_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupDto.builder().name("SomeGroup").build();

        // when
        doThrow(RepositoryException.class).when(groupRepository).addGroup(group);

        // then
        assertThrows(ServiceException.class, () -> groupService.addGroup(groupDto), "Group wasn`t added");
    }

    @Test
    void MethodGetAllGroups_ShouldReturnAllGroupsFromDb() throws RepositoryException, ServiceException {
        var groups = List.of(
            Group.builder().groupName("SomeGroup1").build(),
            Group.builder().groupName("SomeGroup2").build(),
            Group.builder().groupName("SomeGroup3").build());

        when(groupRepository.getAllGroups()).thenReturn(groups);

        var expected = List.of(
            GroupDto.builder().name("SomeGroup1").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup2").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup3").studentList(List.of()).build());

        var actual = groupService.getGroups();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllGroups_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getAllGroups();

        assertThrows(ServiceException.class, () -> groupService.getGroups(), "Groups weren`t received");
    }

    @Test
    void MethodGetGroupById_ShouldReturnGroupFromDb() throws RepositoryException, ServiceException {
        var group = Group.builder().groupName("SomeGroup").build();
        when(groupRepository.getGroup(1)).thenReturn(group);

        var expected = GroupDto.builder().name("SomeGroup").studentList(List.of()).build();

        var actual = groupService.getGroup(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupById_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getGroup(1);

        assertThrows(ServiceException.class, () -> groupService.getGroup(1), "Group wasn`t received");
    }

    @Test
    void MethodGetGroupByName_ShouldReturnGroupFromDb() throws RepositoryException, ServiceException {
        var group = Group.builder().groupName("SomeGroup").build();
        when(groupRepository.getGroup("SomeGroup")).thenReturn(group);

        var expected = GroupDto.builder().name("SomeGroup").studentList(List.of()).build();

        var actual = groupService.getGroup("SomeGroup");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByName_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getGroup("SomeGroup");

        assertThrows(ServiceException.class, () -> groupService.getGroup("SomeGroup"), "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsSmallerThen_ShouldReturnGroupsFromDb() throws RepositoryException, ServiceException {
        var groups = List.of(
            Group.builder().groupName("SomeGroup1").build(),
            Group.builder().groupName("SomeGroup2").build(),
            Group.builder().groupName("SomeGroup3").build());

        when(groupRepository.getGroupsLessThen(3)).thenReturn(groups);

        var expected = List.of(
            GroupDto.builder().name("SomeGroup1").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup2").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup3").studentList(List.of()).build());

        var actual = groupService.getGroupsLessThen(3);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsSmallerThen_ShouldThrowException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getGroupsLessThen(3);

        assertThrows(ServiceException.class, () -> groupService.getGroupsLessThen(3), "Groups weren`t received");
    }

    @Test
    void MethodGetStudentsByGroup_ShouldReturnStudentsFromDb() throws RepositoryException, ServiceException {
        var studentList = List.of(
            Student.builder().groupId(1).firstName("SomeName1").lastName("SomeLastName1").build(),
            Student.builder().groupId(1).firstName("SomeName2").lastName("SomeLastName2").build(),
            Student.builder().groupId(1).firstName("SomeName3").lastName("SomeLastName3").build());

        var group = Group.builder().groupId(1).groupName("SomeGroup").build();

        when(groupRepository.getStudents(group.groupId())).thenReturn(studentList);

        var expected = List.of(
            StudentDto.builder().group(GroupMapper.toGroupDto(group)).firstName("SomeName1").lastName("SomeLastName1").coursesList(List.of()).build(),
            StudentDto.builder().group(GroupMapper.toGroupDto(group)).firstName("SomeName2").lastName("SomeLastName2").coursesList(List.of()).build(),
            StudentDto.builder().group(GroupMapper.toGroupDto(group)).firstName("SomeName3").lastName("SomeLastName3").coursesList(List.of()).build());

        var actual = groupService.getStudentsByGroup(group.groupId());

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByGroupShould_TrowException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getStudents(1);

        assertThrows(ServiceException.class, () -> groupService.getStudentsByGroup(1), "Students weren`t received");
    }

    @Test
    void methodIfExist_ShouldReturnTrueWhenGroupExist() throws RepositoryException, ServiceException {
        var group = Group.builder().groupName("SomeGroup").build();
        when(groupRepository.getGroup(group.groupName())).thenReturn(group);

        assertTrue(groupService.ifExist(group.groupName()));
    }

    @Test
    void methodIfExistShouldReturn_FalseWhenGroupDoesNotExist() throws RepositoryException, ServiceException {
        doThrow(RepositoryException.class).when(groupRepository).getGroup(anyString());

        assertFalse(groupService.ifExist(anyString()));
    }
}
