package ua.com.foxstudent102052.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private GroupService groupService;

    @Mock
    private CourseService courseService;

    private StudentController studentController;

    @BeforeEach
    void setUp() {
        studentController = new StudentController(studentService, groupService, courseService);
    }

    @Test
    void MethodAddStudent_ShouldPassStudentToService() {
        // when
        studentController.addStudent(new StudentDto());

        // then
        verify(studentService).addStudent(new StudentDto());
    }

    @Test
    void MethodAddStudentToCourse_ShouldPassToServiceValues() {
        // when
        studentController.addStudentToCourse(1, 1);

        // then
        verify(studentService).addStudentToCourse(1, 1);
    }

    @Test
    void MethodRemoveStudent_ShouldPassToServiceValues() {
        // when
        studentController.removeStudent(1);

        // then
        verify(studentService).removeStudent(1);
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldPassToServiceValues() {
        // when
        studentController.removeStudentFromCourse(1, 1);

        // then
        verify(studentService).removeStudentFromCourse(1, 1);
    }

    @Test
    void MethodGetStudents_ShouldReturnListOfStudentsWithCoursesAndGroup() {
        // given
        var group = GroupDto.builder().id(1).name("Java").build();
        var studentsDto = List.of(StudentDto
                .builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .group(group)
                .build(),
            StudentDto.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .group(group)
                .build());
        var courses = List.of(
            CourseDto
                .builder()
                .id(1)
                .name("Java")
                .description("Java course")
                .build(),
            CourseDto
                .builder()
                .id(2)
                .name("C#")
                .description("C# course")
                .build());

        var expected = List.of(
            StudentDto
                .builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .group(group)
                .coursesList(courses)
                .build(),
            StudentDto.builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .group(group)
                .coursesList(courses)
                .build());

        // when
        when(studentService.getAll()).thenReturn(studentsDto);
        when(courseService.getCoursesByStudent(anyInt())).thenReturn(courses);
        when(groupService.getGroupById(1)).thenReturn(group);

        // then
        var actual = studentController.getAllStudents();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudents_ShouldPassToServiceValue() {
        // when
        studentController.getAllStudents();

        // then
        verify(studentService).getAll();
    }

    @Test
    void MethodGetStudents_ShouldReturnListOfStudentsWithCoursesAndGroup_ByNameAndCourseId() {
        // given
        var group = GroupDto.builder().id(1).name("Java").build();
        var studentsDto = List.of(StudentDto
                .builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .group(group)
                .build(),
            StudentDto.builder()
                .id(2)
                .firstName("John")
                .lastName("Fox")
                .group(group)
                .build());
        var courses = List.of(
            CourseDto
                .builder()
                .id(1)
                .name("Java")
                .description("Java course")
                .build(),
            CourseDto
                .builder()
                .id(2)
                .name("C#")
                .description("C# course")
                .build());

        var expected = List.of(
            StudentDto
                .builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .group(group)
                .coursesList(courses)
                .build(),
            StudentDto.builder()
                .id(2)
                .firstName("John")
                .lastName("Fox")
                .group(group)
                .coursesList(courses)
                .build());

        // when
        when(studentService.getStudentsByNameAndCourse(anyString(), anyInt())).thenReturn(studentsDto);
        when(courseService.getCoursesByStudent(anyInt())).thenReturn(courses);
        when(groupService.getGroupById(1)).thenReturn(group);

        // then
        var actual = studentController.getStudents(anyString(), anyInt());

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudents_ShouldPassToServiceValues() {
        // when
        studentController.getStudents("John", 1);

        // then
        verify(studentService).getStudentsByNameAndCourse(anyString(), anyInt());
    }
}
