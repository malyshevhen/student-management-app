package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.utils.FileUtils;

@JdbcTest
class CourseDaoImplTest {

    private final CourseDao courseDao;
    private final PostDAO postDAO;
    private final FileUtils fileUtils;

    @Autowired
    public CourseDaoImplTest(DataSource dataSource) {
        this.courseDao = new CourseDaoImpl(dataSource);
        this.postDAO = new PostDaoImpl(dataSource);
        fileUtils = new FileUtils();

    }

    @BeforeEach
    public void setUp() throws DAOException {
        var ddlScript = fileUtils.readFileFromResourcesAsString("scripts/ddl/Table_creation.sql");
        var dmlScript = fileUtils.readFileFromResourcesAsString("scripts/dml/testDB_Data.sql");

        postDAO.doPost(ddlScript);
        postDAO.doPost(dmlScript);
    }

    @Test
    void MethodAddCourse_ShouldAddCourseToDb() throws DAOException {
        // given
        var course = Course.builder()
                .name("Course 4")
                .description("Some description for course 4")
                .build();

        // when
        courseDao.addCourse(course);

        // then
        var allCourses = courseDao.getCourses();

        assertEquals(4, allCourses.size());
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() throws DAOException {
        // when
        var allCourses = courseDao.getCourses();

        // then
        assertEquals(3, allCourses.size());
    }

    @Test
    void MethodGetCourse_ById_ShouldReturnCourseFromDb() throws DAOException {
        // given
        var expected = Course.builder()
                .id(1)
                .name("Course 1")
                .description("Some description for course 1")
                .build();

        // when
        var actual = courseDao.getCourse(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourse_ByName_ShouldReturnCourseFromDb() throws DAOException {
        // given
        var expected = Course.builder()
                .id(1)
                .name("Course 1")
                .description("Some description for course 1")
                .build();

        // when
        var actual = courseDao.getCourse(expected.getName()).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() throws DAOException {
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
        var actual = courseDao.getCourses(2);

        // then
        assertEquals(expected, actual);
    }
}
