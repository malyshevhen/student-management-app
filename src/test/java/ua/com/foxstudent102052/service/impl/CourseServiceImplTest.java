package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.mapper.CourseModelMapper;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {
    private CourseDao courseDao;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        courseDao = mock(CourseDao.class);
        courseService = new CourseServiceImpl(courseDao);
    }

    @Test
    void MethodAddCourse_ShouldPassCourseToRepository() throws DAOException, ServiceException {
        // given
        Course courseFromDb = Course.builder().courseName("Java").courseDescription("Java course").build();
        CourseDto newCourse = CourseDto.builder().name("Java").description("Java course").build();

        // when
        courseService.addCourse(newCourse);

        // then
        verify(courseDao).addCourse(courseFromDb);
    }

    @Test
    void MethodAddCourse_ShouldThrowEnException_WhenCheckIsFailed() throws DAOException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        doThrow(DAOException.class).when(courseDao).addCourse(any(Course.class));

        // then
        assertThrows(ServiceException.class, () -> courseService.addCourse(CourseModelMapper.toCourseDto(course)),
            "Course wasn`t added");
    }

    @Test
    void MethodAddCourse_ShouldThrowEnException_WhenRepositoryExceptionIsThrown() throws DAOException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        doThrow(DAOException.class).when(courseDao).addCourse(course);

        // then
        assertThrows(ServiceException.class, () -> courseService.addCourse(CourseModelMapper.toCourseDto(course)),
            "Course with id 1 already exist");
    }

    @Test
    void MethodGetAllCourses_ShouldReturnListOfAllStudents() throws DAOException, ServiceException {
        // given
        var courses = List.of(
            Course.builder().courseName("Java").courseDescription("Java course").build(),
            Course.builder().courseName("C++").courseDescription("C++ course").build(),
            Course.builder().courseName("C#").courseDescription("C# course").build());

        // when
        when(courseDao.getCourses()).thenReturn(courses);

        // then
        var expected = List.of(
            CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build(),
            CourseDto.builder().name("C++").description("C++ course").studentsList(List.of()).build(),
            CourseDto.builder().name("C#").description("C# course").studentsList(List.of()).build());

        var actual = courseService.getCourses();
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetAllCourses_ShouldThrowException_WhenRepositoryExceptionThrown() throws DAOException {
        // when
        when(courseDao.getCourses()).thenThrow(DAOException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourses(), "Courses weren`t found");
    }

    @Test
    void MethodGetCourseById_ShouldReturnCourseFromDb() throws DAOException, ServiceException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseDao.getCourse(1)).thenReturn(Optional.of(course));

        // then
        var expected = CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build();
        var actual = courseService.getCourse(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourseById_ShouldThrowException_WhenRepositoryExceptionThrown() throws DAOException {
        // when
        when(courseDao.getCourse(1)).thenThrow(DAOException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourse(1), "Course wasn`t found");
    }

    @Test
    void MethodGetCoursesByStudentId_ShouldReturnCourseFromDb() throws DAOException, ServiceException {
        // given
        var course = Course.builder().courseName("Java").courseDescription("Java course").build();

        // when
        when(courseDao.getCourses(1)).thenReturn(List.of(course));

        // then
        var expected = List.of(CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build());
        var actual = courseService.getCourses(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCoursesByStudentId_ShouldThrowException_WhenRepositoryExceptionThrown() throws DAOException {
        // when
        when(courseDao.getCourses(1)).thenThrow(DAOException.class);

        // then
        assertThrows(ServiceException.class, () -> courseService.getCourses(1), "Courses weren`t found");
    }
}
