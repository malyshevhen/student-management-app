package ua.com.foxstudent102052.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.ServiceException;
import ua.com.foxstudent102052.service.StudentService;

public class StudentControllerTest {
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
    void MethodAddStudentShouldPassStudentToService() throws ControllerException, ServiceException {
        // when
        studentController.addStudent(new StudentDto());

        // then
        verify(studentService).addStudent(new StudentDto());
    }

    @Test
    void MethodAddStudentShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doThrow(ServiceException.class).when(studentService).addStudent(StudentMapper.toDto(student));

        // then
        assertThrows(ControllerException.class, () -> studentController.addStudent(StudentMapper.toDto(student)),
                "Student wasn`t added");
    }

    @Test
    void MethodAddStudentToCourseShouldPassToServiceValues() throws ServiceException, ControllerException {
        // when
        studentController.addStudentToCourse(1, 1);

        // then
        verify(studentService).addStudentToCourse(1, 1);
    }

    @Test
    void MethodAddStudentToCourseShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(studentService).addStudentToCourse(1, 1);

        // then
        assertThrows(ControllerException.class, () -> studentController.addStudentToCourse(1, 1),
                "Student wasn`t added to course");
    }

    @Test
    void MethodRemoveStudentShouldPassToServiceValues() throws ServiceException, ControllerException {
        // when
        studentController.removeStudent(1);

        // then
        verify(studentService).removeStudent(1);
    }

    @Test
    void MethodRemoveStudentShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(studentService).removeStudent(1);

        // then
        assertThrows(ControllerException.class, () -> studentController.removeStudent(1), "Student wasn`t removed");
    }

    @Test
    void MethodRemoveStudentFromCourseShouldPassToServiceValues() throws ServiceException, ControllerException {
        // when
        studentController.removeStudentFromCourse(1, 1);

        // then
        verify(studentService).removeStudentFromCourse(1, 1);
    }

    @Test
    void MethodRemoveStudentFromCourseShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(studentService).removeStudentFromCourse(1, 1);

        // then
        assertThrows(ControllerException.class, () -> studentController.removeStudentFromCourse(1, 1),
                "Student wasn`t removed from course");
    }

    @Test
    void MethodGetAllStudentsShouldReturnListOfDTOStudentSWithCoursesAndGroup()
            throws ServiceException, ControllerException {
        // given
        var studentsDto = List.of(StudentDto
                .builder()
                .id(1)
                .groupId(1)
                .firstName("John")
                .lastName("Doe")
                .build(),
                StudentDto.builder()
                        .id(2)
                        .groupId(1)
                        .firstName("Jane")
                        .lastName("Doe")
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
        var group = GroupDto.builder().id(1).name("Java").build();

        var expected = List.of(
                StudentDto
                        .builder()
                        .id(1)
                        .groupId(1)
                        .firstName("John")
                        .lastName("Doe")
                        .coursesList(courses)
                        .group(group.getName())
                        .build(),
                StudentDto.builder()
                        .id(2)
                        .groupId(1)
                        .firstName("Jane")
                        .lastName("Doe")
                        .coursesList(courses)
                        .group(group.getName())
                        .build());

        // when
        when(studentService.getAllStudents()).thenReturn(studentsDto);
        when(courseService.getCoursesByStudentId(anyInt())).thenReturn(courses);
        when(groupService.getGroupById(1)).thenReturn(group);

        // then
        var actual = studentController.getAllStudents();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllStudentsShouldPassToServiceValue() throws ServiceException, ControllerException {
        // when
        studentController.getAllStudents();

        // then
        verify(studentService).getAllStudents();
    }

    @Test
    void MethodGetAllStudentsShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(studentService).getAllStudents();

        // then
        assertThrows(ControllerException.class, () -> studentController.getAllStudents(),
                "Students weren`t found");
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldReturnListOfDTOStudentSWithCoursesAndGroup()
            throws ServiceException, ControllerException {
        // given
        var studentsDto = List.of(StudentDto
                .builder()
                .id(1)
                .groupId(1)
                .firstName("John")
                .lastName("Doe")
                .build(),
                StudentDto.builder()
                        .id(2)
                        .groupId(1)
                        .firstName("John")
                        .lastName("Fox")
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
        var group = GroupDto.builder().id(1).name("Java").build();

        var expected = List.of(
                StudentDto
                        .builder()
                        .id(1)
                        .groupId(1)
                        .firstName("John")
                        .lastName("Doe")
                        .coursesList(courses)
                        .group(group.getName())
                        .build(),
                StudentDto.builder()
                        .id(2)
                        .groupId(1)
                        .firstName("John")
                        .lastName("Fox")
                        .coursesList(courses)
                        .group(group.getName())
                        .build());

        // when
        when(studentService.getStudentsByNameAndCourse(anyString(), anyInt())).thenReturn(studentsDto);
        when(courseService.getCoursesByStudentId(anyInt())).thenReturn(courses);
        when(groupService.getGroupById(1)).thenReturn(group);

        // then
        var actual = studentController.getStudentsByNameAndCourse(anyString(), anyInt());

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldPassToServiceValues() throws ServiceException, ControllerException {
        // when
        studentController.getStudentsByNameAndCourse("John", 1);

        // then
        verify(studentService).getStudentsByNameAndCourse("John", 1);
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(studentService).getStudentsByNameAndCourse("John", 1);

        // then
        assertThrows(ControllerException.class, () -> studentController.getStudentsByNameAndCourse("John", 1),
                "Students weren`t found");
    }
}
