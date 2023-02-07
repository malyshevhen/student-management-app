package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.entity.Course;

class CourseDaoTest extends AbstractTestContainerIT {

    @Autowired
    private CourseDao courseDao;

    @Test
    void MethodAddCourse_ShouldAddCourseToDb() {
        // given
        var course = Course.builder()
                .courseName("Course 4")
                .courseDescription("Some courseDescription for course 4")
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
                .courseId(1)
                .courseName("Course 1")
                .courseDescription("Some description for course 1")
                .build();

        // when
        var actual = courseDao.findById(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourse_ByName_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
                .courseId(1)
                .courseName("Course 1")
                .courseDescription("Some description for course 1")
                .build();

        // when
        var actual = courseDao.findByName(expected.getCourseName()).orElseThrow();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() {
        // given
        var expected = List.of(
                Course.builder()
                        .courseId(1)
                        .courseName("Course 1")
                        .courseDescription("Some description for course 1")
                        .build(),
                Course.builder()
                        .courseId(2)
                        .courseName("Course 2")
                        .courseDescription("Some description for course 2")
                        .build());

        // when
        var actual = courseDao.findByStudentId(2);

        // then
        assertEquals(expected, actual);
    }
}
