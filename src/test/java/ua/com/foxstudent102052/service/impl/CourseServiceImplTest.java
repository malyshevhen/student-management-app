package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private CourseDao courseDao;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        courseDao = mock(CourseDao.class);
        courseService = new CourseServiceImpl(courseDao);
    }

    @Test
    void MethodAddCourse_ShouldPassCourseToRepository() throws DAOException, ElementAlreadyExistException {
        // given
        Course courseFromDb = Course.builder().name("Java").description("Java course").build();
        CourseDto newCourse = modelMapper.map(courseFromDb, CourseDto.class);

        // when
        courseService.addCourse(newCourse);

        // then
        verify(courseDao).addCourse(courseFromDb);
    }

    @Test
    void MethodGetAllCourses_ShouldReturnListOfAllStudents() throws DAOException, ElementAlreadyExistException {
        // given
        var courses = List.of(
            Course.builder().name("Java").description("Java course").build(),
            Course.builder().name("C++").description("C++ course").build(),
            Course.builder().name("C#").description("C# course").build());

        // when
        when(courseDao.getCourses()).thenReturn(courses);

        // then
        var expected = courses.stream()
            .map(course -> modelMapper.map(course, CourseDto.class))
            .toList();

        var actual = courseService.getCourses();
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourseById_ShouldReturnCourseFromDb() throws DAOException, ElementAlreadyExistException {
        // given
        var course = Course.builder().name("Java").description("Java course").build();

        // when
        when(courseDao.getCourse(1)).thenReturn(Optional.of(course));

        // then
        var expected = CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build();
        var actual = courseService.getCourse(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCoursesByStudentId_ShouldReturnCourseFromDb() throws DAOException, ElementAlreadyExistException {
        // given
        var course = Course.builder().name("Java").description("Java course").build();

        // when
        when(courseDao.getCourses(1)).thenReturn(List.of(course));

        // then
        var expected = List.of(CourseDto.builder().name("Java").description("Java course").studentsList(List.of()).build());
        var actual = courseService.getCourses(1);

        assertEquals(expected, actual);
    }
}
