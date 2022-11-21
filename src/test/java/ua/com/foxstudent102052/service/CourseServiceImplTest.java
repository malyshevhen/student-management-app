package ua.com.foxstudent102052.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.repository.CourseRepository;
import ua.com.foxstudent102052.repository.RepositoryException;

class CourseServiceImplTest {
    private CourseRepository courseRepository;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        courseRepository = mock(CourseRepository.class);
        courseService = new CourseServiceImpl(courseRepository);
    }

    @Test
    void MethodAddCourseShouldPassCourseToRepository() throws RepositoryException, ServiceException {
        // given
        Course courseFromDb = Course.builder().courseName("Java").courseDescription("Java course").build();
        CourseDto newCourse = CourseDto.builder().name("Java").description("Java course").build();
        // when
        when(courseRepository.getLastCourse()).thenReturn(courseFromDb);
        courseService.addCourse(newCourse);

        // then
        verify(courseRepository).addCourse(courseFromDb);
    }

    @Test
    void MethodAddCourseShouldThrowEnExceptionWhenCheckIsFailed() throws RepositoryException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseRepository.getLastCourse()).thenReturn(new Course());

        // then
        assertThrows(ServiceException.class, () -> courseService.addCourse(CourseMapper.toDto(course)),
                "Course wasn`t added");
    }

    @Test
    void MethodAddCourseShouldThrowEnExceptionWhenRepositoryExceptionIsThrown() throws RepositoryException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        doThrow(RepositoryException.class).when(courseRepository).addCourse(course);

        // then
        assertThrows(ServiceException.class, () -> courseService.addCourse(CourseMapper.toDto(course)),
                "Course with id 1 already exist");
    }

    @Test
    void MethodGetAllCoursesShouldReturnListOfAllStudents() throws RepositoryException, ServiceException {
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

        var actual = courseService.getAllCourses();
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllCoursesShouldThrowExceptionWhenRepositoryExceptionThrown() throws RepositoryException {
        // when
        when(courseRepository.getAllCourses()).thenThrow(RepositoryException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getAllCourses(), "Courses weren`t found");
    }

    @Test
    void MethodGetCourseByIdShouldReturnCourseFromDb() throws RepositoryException, ServiceException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseRepository.getCourseById(1)).thenReturn(course);

        // then
        var expected = CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build();
        var actual = courseService.getCourseById(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourseByIdShouldThrowExceptionWhenRepositoryExceptionThrown() throws RepositoryException {
        // when
        when(courseRepository.getCourseById(1)).thenThrow(RepositoryException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourseById(1), "Course wasn`t found");
    }

    @Test
    void MethodGetCoursesByStudentIdShouldReturnCourseFromDb() throws RepositoryException, ServiceException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseRepository.getCoursesByStudentId(1)).thenReturn(List.of(course));

        // then
        var expected = List.of(CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build());
        var actual = courseService.getCoursesByStudentId(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCoursesByStudentIdShouldThrowExceptionWhenRepositoryExceptionThrown() throws RepositoryException {
        // when
        when(courseRepository.getCoursesByStudentId(1)).thenThrow(RepositoryException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCoursesByStudentId(1), "Courses weren`t found");
    }
}
