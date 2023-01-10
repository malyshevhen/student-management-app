package ua.com.foxstudent102052.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.utils.FileUtils;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class CourseDaoImplTest {
    private CustomDataSource customDataSource;
    private CourseDao courseDao;

    @BeforeEach
    public void setUp() throws DAOException {
        customDataSource = PooledDataSource.getInstance();
        courseDao = new CourseDaoImpl(customDataSource);

        FileUtils fileUtils = new FileUtils();
        var ddlScript = fileUtils.readFileFromResourcesAsString("scripts/ddl/Table_creation.sql");
        var dmlScript = fileUtils.readFileFromResourcesAsString("scripts/dml/testDB_Data.sql");

        PostDAO postDAO = new PostDAOImpl(customDataSource);
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
    void MethodAddCourse_ShouldThrowException_WhenSQLExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        courseDao = new CourseDaoImpl(customDataSource);
        var course = new Course(0, " ", " ");

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> courseDao.addCourse(course),
            "Error while adding student to the database");
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() throws DAOException {
        // when
        var allCourses = courseDao.getCourses();

        // then
        assertEquals(3, allCourses.size());
    }

    @Test
    void MethodGetCourses_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        courseDao = new CourseDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> courseDao.getCourses(),
            "Error while adding student to the database");
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
    void MethodGetCourse_ById_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        courseDao = new CourseDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> courseDao.getCourse(1),
            "Error while adding student to the database");
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
    void MethodGetCourse_ByName_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        courseDao = new CourseDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> courseDao.getCourse(""),
            "Error while adding student to the database");
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
                .build()
            );

        // when
        var actual = courseDao.getCourses(2);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        courseDao = new CourseDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> courseDao.getCourses(1),
            "Error while adding student to the database");
    }
}
