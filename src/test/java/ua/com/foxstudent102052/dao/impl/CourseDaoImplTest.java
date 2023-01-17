package ua.com.foxstudent102052.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.entity.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Sql({"/scripts/ddl/Table_creation.sql", "/scripts/dml/testDB_Data.sql"})
class CourseDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CourseDao courseDao;

    @BeforeEach
    void setUp() {
        courseDao = new CourseDaoImpl(jdbcTemplate);
    }


    @Test
    void MethodAddCourse_ShouldAddCourseToDb() {
        // given
        var course = Course.builder()
            .name("Course 4")
            .description("Some description for course 4")
            .build();

        // when
        courseDao.addCourse(course);

        // then
        var allCourses = courseDao.getAll();

        assertEquals(4, allCourses.size());
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() {
        // when
        var allCourses = courseDao.getAll();

        // then
        assertEquals(3, allCourses.size());
    }

    @Test
    void MethodGetCourse_ById_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
            .id(1)
            .name("Course 1")
            .description("Some description for course 1")
            .build();

        // when
        var actual = courseDao.getCourseById(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourse_ByName_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
            .id(1)
            .name("Course 1")
            .description("Some description for course 1")
            .build();

        // when
        var actual = courseDao.getCourseByName(expected.getName()).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() {
        // given
        var expected = List.of(
            Course.builder()
                .id(1)
                .name("Course 1")
                .description("Some description for course 1")
                .build(),
            Course.builder()
                .id(2)
                .name("Course 2")
                .description("Some description for course 2")
                .build());

        // when
        var actual = courseDao.getCoursesByStudentId(2);

        // then
        assertEquals(expected, actual);
    }
}
