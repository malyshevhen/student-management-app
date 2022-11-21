package ua.com.foxstudent102052.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.GroupRepository;
import ua.com.foxstudent102052.repository.RepositoryException;

public class GroupServiceImplTest {
    private GroupRepository groupRepository;
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        groupRepository = mock(GroupRepository.class);
        groupService = new GroupServiceImpl(groupRepository);
    }

    @Test
    void MethodAddGroupShouldPassGroupToRepository() throws RepositoryException, ServiceException {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupDto.builder().name("SomeGroup").build();

        // when
        doThrow(RepositoryException.class).when(groupRepository).getGroupByName(anyString());
        when(groupRepository.getLastGroup()).thenReturn(group);
        groupService.addGroup(groupDto);

        // then
        verify(groupRepository).addGroup(group);
    }

    @Test
    void MethodAddGroupShouldThrowExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupDto.builder().name("SomeGroup").build();

        // when
        when(groupRepository.getLastGroup()).thenReturn(group);
        doThrow(RepositoryException.class).when(groupRepository).addGroup(group);

        // then
        assertThrows(ServiceException.class, () -> groupService.addGroup(groupDto), "Group wasn`t added");
    }

    @Test
    void MethodGetAllGroupsShouldReturnAllGroupsFromDb() throws RepositoryException, ServiceException {
        var groups = List.of(
            Group.builder().groupName("SomeGroup1").build(),
            Group.builder().groupName("SomeGroup2").build(),
            Group.builder().groupName("SomeGroup3").build());

        when(groupRepository.getAllGroups()).thenReturn(groups);

        var expected = List.of(
            GroupDto.builder().name("SomeGroup1").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup2").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup3").studentList(List.of()).build());

        var actual = groupService.getAllGroups();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllGroupsShouldThrowExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getAllGroups();

        assertThrows(ServiceException.class, () -> groupService.getAllGroups(), "Groups weren`t received");
    }

    @Test
    void MethodGetGroupByIdShouldReturnGroupFromDb() throws RepositoryException, ServiceException {
        var group = Group.builder().groupName("SomeGroup").build();
        when(groupRepository.getGroupById(1)).thenReturn(group);

        var expected = GroupDto.builder().name("SomeGroup").studentList(List.of()).build();

        var actual = groupService.getGroupById(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByIdShouldThrowExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getGroupById(1);

        assertThrows(ServiceException.class, () -> groupService.getGroupById(1), "Group wasn`t received");
    }

    @Test
    void MethodGetGroupByNameShouldReturnGroupFromDb() throws RepositoryException, ServiceException {
        var group = Group.builder().groupName("SomeGroup").build();
        when(groupRepository.getGroupByName("SomeGroup")).thenReturn(group);

        var expected = GroupDto.builder().name("SomeGroup").studentList(List.of()).build();

        var actual = groupService.getGroupByName("SomeGroup");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByNameShouldThrowExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getGroupByName("SomeGroup");

        assertThrows(ServiceException.class, () -> groupService.getGroupByName("SomeGroup"), "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsSmallerThenShouldReturnGroupsFromDb() throws RepositoryException, ServiceException {
        var groups = List.of(
            Group.builder().groupName("SomeGroup1").build(),
            Group.builder().groupName("SomeGroup2").build(),
            Group.builder().groupName("SomeGroup3").build());

        when(groupRepository.getGroupsSmallerThen(3)).thenReturn(groups);

        var expected = List.of(
            GroupDto.builder().name("SomeGroup1").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup2").studentList(List.of()).build(),
            GroupDto.builder().name("SomeGroup3").studentList(List.of()).build());

        var actual = groupService.getGroupsSmallerThen(3);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsSmallerThenThrowExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getGroupsSmallerThen(3);

        assertThrows(ServiceException.class, () -> groupService.getGroupsSmallerThen(3), "Groups weren`t received");
    }

    @Test
    void MethodGetStudentsByGroupShouldReturnStudentsFromDb() throws RepositoryException, ServiceException {
        var studentList = List.of(
            Student.builder().groupId(1).firstName("SomeName1").lastName("SomeLastName1").build(),
            Student.builder().groupId(1).firstName("SomeName2").lastName("SomeLastName2").build(),
            Student.builder().groupId(1).firstName("SomeName3").lastName("SomeLastName3").build());

        var group = Group.builder().groupId(1).groupName("SomeGroup").build();

        when(groupRepository.getStudentsByGroup(group.getGroupId())).thenReturn(studentList);

        var expected = List.of(
            StudentDto.builder().groupId(1).group("").firstName("SomeName1").lastName("SomeLastName1").coursesList(List.of()).build(),
            StudentDto.builder().groupId(1).group("").firstName("SomeName2").lastName("SomeLastName2").coursesList(List.of()).build(),
            StudentDto.builder().groupId(1).group("").firstName("SomeName3").lastName("SomeLastName3").coursesList(List.of()).build());

        var actual = groupService.getStudentsByGroup(group.getGroupId());

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByGroupShouldTrowExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        doThrow(RepositoryException.class).when(groupRepository).getStudentsByGroup(1);

        assertThrows(ServiceException.class, () -> groupService.getStudentsByGroup(1), "Students weren`t received");
    }

    @Test
    void methodIfExistShouldReturnTrueWhenGroupExist() throws RepositoryException, ServiceException {
        var group = Group.builder().groupName("SomeGroup").build();
        when(groupRepository.getGroupByName(group.getGroupName())).thenReturn(group);

        assertTrue(groupService.ifExist(group.getGroupName()));
    }

    @Test
    void methodIfExistShouldReturnFalseWhenGroupDoesNotExist() throws RepositoryException, ServiceException {
        doThrow(RepositoryException.class).when(groupRepository).getGroupByName(anyString());

        assertFalse(groupService.ifExist(anyString()));
    }
}
