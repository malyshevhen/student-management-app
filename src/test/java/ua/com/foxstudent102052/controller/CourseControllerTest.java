package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void MethodGetAllCourses_ShouldReturnListOfAllCourses() throws ElementAlreadyExistException {
        // given
        var studentDtoList = List.of(
            StudentDto.builder().id(1).firstName("John").lastName("Doe").build(),
            StudentDto.builder().id(2).firstName("Jane").lastName("Doe").build(),
            StudentDto.builder().id(3).firstName("Jack").lastName("Doe").build()
        );
        var expected = List.of(
            CourseDto.builder().id(1).name("Java").description("Java course").studentList(studentDtoList).build(),
            CourseDto.builder().id(2).name("C++").description("C# course").studentList(studentDtoList).build(),
            CourseDto.builder().id(3).name("Python").description("Python course").studentList(studentDtoList).build()
        );

        // when
        when(studentService.getStudents()).thenReturn(studentDtoList);
        when(courseService.getCourses()).thenReturn(expected);

        // then
        var actual = courseController.getAllCourses();

        assertEquals(expected, actual);
    }
}
