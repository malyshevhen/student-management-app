package ua.com.foxstudent102052.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    private CourseController courseController;

    @BeforeEach
    void setUp() {
        courseController = new CourseController(courseService, studentService);
    }

    @Test
    void MethodGetAllCourses_ShouldReturnListOfAllCourses() {
        // given
        var studentDtoList = List.of(
                StudentDto.builder()
                        .studentId(1)
                        .firstName("John")
                        .lastName("Doe")
                        .build(),
                StudentDto.builder()
                        .studentId(2)
                        .firstName("Jane")
                        .lastName("Doe")
                        .build(),
                StudentDto.builder()
                        .studentId(3)
                        .firstName("Jack")
                        .lastName("Doe")
                        .build());
        var expected = List.of(
                CourseDto.builder()
                        .courseId(1)
                        .courseName("Java")
                        .courseDescription("Java course")
                        .studentList(studentDtoList)
                        .build(),
                CourseDto.builder()
                        .courseId(2)
                        .courseName("C++")
                        .courseDescription("C# course")
                        .studentList(studentDtoList)
                        .build(),
                CourseDto.builder()
                        .courseId(3)
                        .courseName("Python")
                        .courseDescription("Python course")
                        .studentList(studentDtoList)
                        .build());

        // when
        when(courseService.getAll()).thenReturn(expected);

        // then
        var actual = courseController.getAllCourses();

        assertEquals(expected, actual);
    }
}
