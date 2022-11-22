package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.ServiceException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void MethodGetAllGroupsShouldReturnListOfAllGroups() throws ServiceException, ControllerException {
        // given
        var students = List.of(
            StudentDto.builder().id(1).groupId(1).firstName("Darth").lastName("Vader").coursesList(List.of()).build(),
            StudentDto.builder().id(2).groupId(1).firstName("Luke").lastName("Skywalker").coursesList(List.of()).build(),
            StudentDto.builder().id(3).groupId(2).firstName("Han").lastName("Solo").coursesList(List.of()).build()
        );
        var expected = List.of(
            GroupDto.builder().id(1).name("Jedi").studentList(students).build(),
            GroupDto.builder().id(2).name("Rebels").studentList(students).build()
        );

        // when
        when(groupService.getAllGroups()).thenReturn(expected);
        var actual = groupController.getAllGroups();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllGroupsShouldThrowControllerException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(groupService).getAllGroups();

        // then
        assertThrows(ControllerException.class, () -> groupController.getAllGroups());
    }

    @Test
    void MethodGetGroupsSmallerThenReturnListOfAllGroups() throws ServiceException, ControllerException {
        // given
        var students = List.of(
            StudentDto.builder().id(1).groupId(1).firstName("Darth").lastName("Vader").coursesList(List.of()).build(),
            StudentDto.builder().id(2).groupId(1).firstName("Luke").lastName("Skywalker").coursesList(List.of()).build(),
            StudentDto.builder().id(3).groupId(2).firstName("Han").lastName("Solo").coursesList(List.of()).build()
        );
        var expected = List.of(
            GroupDto.builder().id(1).name("Jedi").studentList(students).build(),
            GroupDto.builder().id(2).name("Rebels").studentList(students).build()
        );

        // when
        when(groupService.getGroupsSmallerThen(anyInt())).thenReturn(expected);
        var actual = groupController.getGroupsSmallerThen(anyInt());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsSmallerThenShouldThrowControllerException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(groupService).getGroupsSmallerThen(anyInt());

        // then
        assertThrows(ControllerException.class, () -> groupController.getGroupsSmallerThen(anyInt()));
    }
}
