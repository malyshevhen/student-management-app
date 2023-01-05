package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GroupControllerTest {
    private GroupService groupService;
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        groupController = new GroupController(groupService);
    }

    @Test
    void MethodGetGroups_ShouldReturnListOfAllGroups() throws ElementAlreadyExistException{
        // given
        var students = List.of(
            StudentDto.builder().id(1).group(GroupDto.builder().id(1).build()).firstName("Darth").lastName("Vader").coursesList(List.of()).build(),
            StudentDto.builder().id(2).group(GroupDto.builder().id(1).build()).firstName("Luke").lastName("Skywalker").coursesList(List.of()).build(),
            StudentDto.builder().id(3).group(GroupDto.builder().id(2).build()).firstName("Han").lastName("Solo").coursesList(List.of()).build()
        );
        var expected = List.of(
            GroupDto.builder().id(1).name("Jedi").studentList(students).build(),
            GroupDto.builder().id(2).name("Rebels").studentList(students).build()
        );

        // when
        when(groupService.getGroups()).thenReturn(expected);
        var actual = groupController.getAllGroups();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnListOfAllGroups() throws ElementAlreadyExistException{
        // given
        var students = List.of(
            StudentDto.builder().id(1).group(GroupDto.builder().id(1).build()).firstName("Darth").lastName("Vader").coursesList(List.of()).build(),
            StudentDto.builder().id(2).group(GroupDto.builder().id(1).build()).firstName("Luke").lastName("Skywalker").coursesList(List.of()).build(),
            StudentDto.builder().id(3).group(GroupDto.builder().id(2).build()).firstName("Han").lastName("Solo").coursesList(List.of()).build()
        );
        var expected = List.of(
            GroupDto.builder().id(1).name("Jedi").studentList(students).build(),
            GroupDto.builder().id(2).name("Rebels").studentList(students).build()
        );

        // when
        when(groupService.getGroupsLessThen(anyInt())).thenReturn(expected);
        var actual = groupController.getGroupsSmallerThen(anyInt());

        // then
        assertEquals(expected, actual);
    }
}
