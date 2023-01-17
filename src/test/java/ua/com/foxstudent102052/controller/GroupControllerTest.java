package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

    private GroupController groupController;

    @BeforeEach
    void setUp() {
        groupController = new GroupController(groupService, studentService);
    }

    @Test
    void MethodGetGroups_ShouldReturnListOfAllGroups() {
        // given
        var students = List.of(
            StudentDto.builder().id(1).group(GroupDto.builder().id(1).build()).firstName("Darth").lastName("Vader")
                .coursesList(List.of()).build(),
            StudentDto.builder().id(2).group(GroupDto.builder().id(1).build()).firstName("Luke")
                .lastName("Skywalker").coursesList(List.of()).build(),
            StudentDto.builder().id(3).group(GroupDto.builder().id(2).build()).firstName("Han").lastName("Solo")
                .coursesList(List.of()).build());
        var expected = List.of(
            GroupDto.builder().id(1).name("Jedi").studentList(students).build(),
            GroupDto.builder().id(2).name("Rebels").studentList(students).build());

        // when
        when(groupService.getAll()).thenReturn(expected);
        var actual = groupController.getAllGroups();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnListOfAllGroups() {
        // given
        var students = List.of(
            StudentDto.builder().id(1).group(GroupDto.builder().id(1).build()).firstName("Darth").lastName("Vader")
                .coursesList(List.of()).build(),
            StudentDto.builder().id(2).group(GroupDto.builder().id(1).build()).firstName("Luke")
                .lastName("Skywalker").coursesList(List.of()).build(),
            StudentDto.builder().id(3).group(GroupDto.builder().id(2).build()).firstName("Han").lastName("Solo")
                .coursesList(List.of()).build());
        var expected = List.of(
            GroupDto.builder().id(1).name("Jedi").studentList(students).build(),
            GroupDto.builder().id(2).name("Rebels").studentList(students).build());

        // when
        when(groupService.getGroupsLessThen(anyInt())).thenReturn(expected);
        var actual = groupController.getGroupsSmallerThen(anyInt());

        // then
        assertEquals(expected, actual);
    }
}
