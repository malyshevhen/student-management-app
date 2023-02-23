package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.CourseRepository;
import ua.com.foxstudent102052.model.entity.Course;

class CourseDaoTest extends AbstractTestContainerIT {

    @Autowired
    private CourseRepository courseDao;

    @Test
    void MethodAddCourse_ShouldAddCourseToDb() {
        // given
        var course = Course.builder()
            .name("Course 4")
            .description("Some courseDescription for course 4")
            .build();

        // when
        courseDao.save(course);

        // then
        var allCourses = courseDao.findAll();

        assertEquals(4, allCourses.size());
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() {
        // when
        var allCourses = courseDao.findAll();

        // then
        assertEquals(3, allCourses.size());
    }

    @Test
    void MethodGetCourse_ById_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
            .id(1L)
            .name("Course 1")
            .description("Some description for course 1")
            .build();

        // when
        var actual = courseDao.findById(1L).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourse_ByName_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
            .id(1L)
            .name("Course 1")
            .description("Some description for course 1")
            .build();

        // when
        var actual = courseDao.findByName(expected.getName()).orElseThrow();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() {
        // given
        var expected = List.of(
            Course.builder()
                .id(1L)
                .name("Course 1")
                .description("Some description for course 1")
                .build(),
            Course.builder()
                .id(2L)
                .name("Course 2")
                .description("Some description for course 2")
                .build()
        );

        // when
        var actual = courseDao.findByStudentId(2L);

        // then
        assertEquals(expected, actual);
    }
}
