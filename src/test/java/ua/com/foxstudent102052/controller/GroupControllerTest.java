package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.service.GroupService;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GroupControllerTest {
    private GroupService groupService;
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        groupController = new GroupController(groupService);
    }

    @DisplayName("Method addGroup() should pass adding command to GroupService")
    @ParameterizedTest
    @MethodSource
    void addGroup(String groupDtoName) {
        Group group = new Group(groupDtoName);
        doNothing().when(groupService).addGroup(group);

        groupController.addGroup(groupDtoName);

        verify(groupService, times(1)).addGroup(group);
    }

    private static Stream<Arguments> addGroup() {
        return Stream.of(
            Arguments.of("Group 1"),
            Arguments.of("Group 2"),
            Arguments.of("Group 3")
        );
    }

    @DisplayName("Method updateGroup() should pass updating command to GroupService")
    @ParameterizedTest
    @MethodSource
    void updateGroup(int groupId, String groupName) {
        Group group = new Group(groupId, groupName);
        doNothing().when(groupService).updateGroup(group);

        groupController.updateGroup(groupId, groupName);

        verify(groupService, times(1)).updateGroup(group);
    }

    private static Stream<Arguments> updateGroup() {
        return Stream.of(
            Arguments.of(1, "Group 1"),
            Arguments.of(2, "Group 2"),
            Arguments.of(3, "Group 3")
        );
    }

    @DisplayName("Method removeGroup() should pass removing command to GroupService")
    @ParameterizedTest
    @MethodSource
    void removeGroup(int groupId) {
        doNothing().when(groupService).removeGroup(groupId);

        groupController.removeGroup(groupId);

        verify(groupService, times(1)).removeGroup(groupId);
    }

    private static Stream<Arguments> removeGroup() {
        return Stream.of(
            Arguments.of(1),
            Arguments.of(2),
            Arguments.of(3)
        );
    }


    @DisplayName("Method getGroupById() should return 'GroupDto' by id")
    @ParameterizedTest
    @MethodSource
    void getGroupById(int groupId) {
        Group group = new Group(groupId, "Group 1");
        when(groupService.getGroupById(groupId)).thenReturn(group);

        groupController.getGroupById(groupId);

        verify(groupService, times(1)).getGroupById(groupId);
    }

    private static Stream<Arguments> getGroupById() {
        return Stream.of(
            Arguments.of(1),
            Arguments.of(2),
            Arguments.of(3)
        );
    }

    @DisplayName("Method getAllGroups() should return 'List<GroupDto>'")
    @Test
    void getAllGroups() {
        var expected = List.of(
            new GroupDto(1, "Group 1", List.of()),
            new GroupDto(2, "Group 2", List.of()),
            new GroupDto(3, "Group 3", List.of())
        );

        var groups = List.of(
            new Group(1, "Group 1"),
            new Group(2, "Group 2"),
            new Group(3, "Group 3")
        );
        when(groupService.getAllGroups()).thenReturn(groups);

        var actual = groupController.getAllGroups();

        assertEquals(expected, actual);
    }
    
    @DisplayName("Method getGroupsSmallerThen() should return 'List<GroupDto>'")
    @Test
    void getGroupsSmallerThen() {
        when(groupService.getGroupsSmallerThen(10)).thenReturn(List.of(
            new Group(1, "Group 1"),
            new Group(2, "Group 2"),
            new Group(3, "Group 3")
        ));
        
        var expected = List.of(
            new GroupDto(1, "Group 1", List.of()),
            new GroupDto(2, "Group 2", List.of()),
            new GroupDto(3, "Group 3", List.of())
        );
        
        var actual = groupController.getGroupsSmallerThen(10);
        
        assertEquals(expected, actual);
    }
    
    @DisplayName("Method getGroupByName() should return 'GroupDto' by name")
    @Test
    void getGroupByName() {
        when(groupService.getGroupByName("Group 1")).thenReturn(new Group(1, "Group 1"));
        
        var expected = new GroupDto(1, "Group 1", List.of());
        
        var actual = groupController.getGroupByName("Group 1");
        
        assertEquals(expected, actual);
    }
}
