package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.dto.CourseDto;
import ua.com.foxstudent102052.repository.interfaces.CourseRepository;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {
    private CourseRepository courseRepository;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        courseRepository = mock(CourseRepository.class);
        courseService = new CourseServiceImpl(courseRepository);
    }

    @Test
    void MethodAddCourse_ShouldPassCourseToRepository() throws RepositoryException, ServiceException {
        // given
        Course courseFromDb = Course.builder().courseName("Java").courseDescription("Java course").build();
        CourseDto newCourse = CourseDto.builder().name("Java").description("Java course").build();

        // when
        courseService.addCourse(newCourse);

        // then
        verify(courseRepository).addCourse(courseFromDb);
    }

    @Test
    void MethodAddCourse_ShouldThrowEnException_WhenCheckIsFailed() throws RepositoryException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when

        // then
        assertThrows(ServiceException.class, () -> courseService.addCourse(CourseMapper.toCourseDto(course)),
            "Course wasn`t added");
    }

    @Test
    void MethodAddCourse_ShouldThrowEnException_WhenRepositoryExceptionIsThrown() throws RepositoryException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        doThrow(RepositoryException.class).when(courseRepository).addCourse(course);

        // then
        assertThrows(ServiceException.class, () -> courseService.addCourse(CourseMapper.toCourseDto(course)),
            "Course with id 1 already exist");
    }

    @Test
    void MethodGetAllCourses_ShouldReturnListOfAllStudents() throws RepositoryException, ServiceException {
        // given
        var courses = List.of(
            Course.builder().courseName("Java").courseDescription("Java course").build(),
            Course.builder().courseName("C++").courseDescription("C++ course").build(),
            Course.builder().courseName("C#").courseDescription("C# course").build());

        // when
        when(courseRepository.getAllCourses()).thenReturn(courses);

        // then
        var expected = List.of(
            CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build(),
            CourseDto.builder().name("C++").description("C++ course").studentsList(List.of()).build(),
            CourseDto.builder().name("C#").description("C# course").studentsList(List.of()).build());

        var actual = courseService.getCourses();
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllCourses_ShouldThrowException_WhenRepositoryExceptionThrown() throws RepositoryException {
        // when
        when(courseRepository.getAllCourses()).thenThrow(RepositoryException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourses(), "Courses weren`t found");
    }

    @Test
    void MethodGetCourseById_ShouldReturnCourseFromDb() throws RepositoryException, ServiceException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseRepository.getCourseById(1)).thenReturn(course);

        // then
        var expected = CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build();
        var actual = courseService.getCourse(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourseById_ShouldThrowException_WhenRepositoryExceptionThrown() throws RepositoryException {
        // when
        when(courseRepository.getCourseById(1)).thenThrow(RepositoryException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourse(1), "Course wasn`t found");
    }

    @Test
    void MethodGetCoursesByStudentId_ShouldReturnCourseFromDb() throws RepositoryException, ServiceException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseRepository.getCoursesByStudentId(1)).thenReturn(List.of(course));

        // then
        var expected = List.of(CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build());
        var actual = courseService.getCourses(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCoursesByStudentId_ShouldThrowException_WhenRepositoryExceptionThrown() throws RepositoryException {
        // when
        when(courseRepository.getCoursesByStudentId(1)).thenThrow(RepositoryException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourses(1), "Courses weren`t found");
    }
}
