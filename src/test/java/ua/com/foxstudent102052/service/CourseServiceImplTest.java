package ua.com.foxstudent102052.service;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.repository.CourseRepository;

public class CourseServiceImplTest {
    private CourseRepository courseRepository;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        courseRepository = mock(CourseRepository.class);
        courseService = new CourseServiceImpl(courseRepository);
    }

    @Test
    void testAddCourse() {

    }

    @Test
    void testGetAllCourses() {

    }

    @Test
    void testGetCourseById() {

    }

    @Test
    void testGetCoursesByStudentId() {

    }
}
