package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.ServiceException;
import ua.com.foxstudent102052.service.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CourseControllerTest {
    private CourseService courseService;
    private StudentService studentService;
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        courseService = mock(CourseService.class);
        studentService = mock(StudentService.class);
        courseController = new CourseController(courseService, studentService);
    }

    @Test
    void MethodGetAllCoursesShouldReturnListOfAllCourses() throws ServiceException, ControllerException {
        // given
        var students = List.of(
            StudentDto.builder().id(1).firstName("John").lastName("Doe").build(),
            StudentDto.builder().id(2).firstName("Jane").lastName("Doe").build(),
            StudentDto.builder().id(3).firstName("Jack").lastName("Doe").build()
        );
        var expected = List.of(
            CourseDto.builder().id(1).name("Java").description("Java course").studentsList(students).build(),
            CourseDto.builder().id(2).name("C++").description("C# course").studentsList(students).build(),
            CourseDto.builder().id(3).name("Python").description("Python course").studentsList(students).build()
        );

        // when
        when(studentService.getAllStudents()).thenReturn(students);
        when(courseService.getAllCourses()).thenReturn(expected);

        // then
        var actual = courseController.getAllCourses();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllCoursesShouldThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(courseService).getAllCourses();

        // then
        assertThrows(ControllerException.class, () -> courseController.getAllCourses(),
                "Couldn`t get all courses");
    }

    @Test
    void MethodGetStudentsByCourseShouldReturnCourseDto() throws ControllerException, ServiceException {
        // given
        var expected = List.of(
            StudentDto.builder().id(1).firstName("John").lastName("Doe").coursesList(List.of()).build(),
            StudentDto.builder().id(2).firstName("Jane").lastName("Doe").coursesList(List.of()).build(),
            StudentDto.builder().id(3).firstName("Jack").lastName("Doe").coursesList(List.of()).build()
        );

        // when
        when(studentService.getStudentsByCourse(anyInt())).thenReturn(expected);

        // then
        var actual = courseController.getStudentsByCourse(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByCourseShoulThrowAnExceptionIfServiceThrowsAnException() throws ServiceException {
        // when
        doThrow(ServiceException.class).when(studentService).getStudentsByCourse(anyInt());

        // then
        assertThrows(ControllerException.class, () -> courseController.getStudentsByCourse(1),
                "Couldn`t get students by course");
    }
}
