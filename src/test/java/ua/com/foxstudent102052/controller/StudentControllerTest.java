package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StudentControllerTest {
    private StudentService studentService;
    private GroupService groupService;
    private CourseService courseService;
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        studentService = mock(StudentService.class);
        groupService = mock(GroupService.class);
        courseService = mock(CourseService.class);
        studentController = new StudentController(studentService, groupService, courseService);
    }

    @Test
    void MethodAddStudent_ShouldPassStudentToService() throws ElementAlreadyExistException {
        // when
        studentController.addStudent(new StudentDto());

        // then
        verify(studentService).addStudent(new StudentDto());
    }

    @Test
    void MethodAddStudentToCourse_ShouldPassToServiceValues() throws ElementAlreadyExistException {
        // when
        studentController.addStudentToCourse(1, 1);

        // then
        verify(studentService).addStudentToCourse(1, 1);
    }

    @Test
    void MethodRemoveStudent_ShouldPassToServiceValues() throws ElementAlreadyExistException {
        // when
        studentController.removeStudent(1);

        // then
        verify(studentService).removeStudent(1);
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldPassToServiceValues() throws ElementAlreadyExistException {
        // when
        studentController.removeStudentFromCourse(1, 1);

        // then
        verify(studentService).removeStudentFromCourse(1, 1);
    }

    @Test
    void MethodGetStudents_ShouldReturnListOfStudentsWithCoursesAndGroup() throws ElementAlreadyExistException {
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
        when(studentService.getStudents()).thenReturn(studentsDto);
        when(courseService.getCourses(anyInt())).thenReturn(courses);
        when(groupService.getGroup(1)).thenReturn(group);

        // then
        var actual = studentController.getAllStudents();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudents_ShouldPassToServiceValue() throws ElementAlreadyExistException {
        // when
        studentController.getAllStudents();

        // then
        verify(studentService).getStudents();
    }

    @Test
    void MethodGetStudents_ShouldReturnListOfStudentsWithCoursesAndGroup_ByNameAndCourseId()
        throws ElementAlreadyExistException {
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
        when(studentService.getStudents(anyString(), anyInt())).thenReturn(studentsDto);
        when(courseService.getCourses(anyInt())).thenReturn(courses);
        when(groupService.getGroup(1)).thenReturn(group);

        // then
        var actual = studentController.getStudents(anyString(), anyInt());

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudents_ShouldPassToServiceValues() throws ElementAlreadyExistException {
        // when
        studentController.getStudents("John", 1);

        // then
        verify(studentService).getStudents("John", 1);
    }
}
