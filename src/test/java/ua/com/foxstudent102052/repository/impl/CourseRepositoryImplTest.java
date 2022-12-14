package ua.com.foxstudent102052.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.datasource.impl.H2CustomDataSource;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.CourseRepository;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class CourseRepositoryImplTest {
    private H2CustomDataSource customDataSource;
    private CourseRepository courseRepository;
    private PostDAO postDAO;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        customDataSource = H2CustomDataSource.getInstance();
        postDAO = new PostDAOImpl(customDataSource);
        courseRepository = new CourseRepositoryImpl(customDataSource);
        var ddlScript = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        var dmlScript = FileUtils.readTextFile("src/test/resources/scripts/dml/testDB_Data.sql");
        postDAO.doPost(ddlScript);
        postDAO.doPost(dmlScript);
    }

    @Test
    void MethodAddCourse_ShouldAddCourseToDb() throws RepositoryException {
        // given
        var course = Course.builder()
            .courseName("Course 4")
            .courseDescription("Some description for course 4")
            .build();

        // when
        courseRepository.addCourse(course);

        // then
        var allCourses = courseRepository.getAllCourses();

        assertEquals(4, allCourses.size());
    }

    @Test
    void MethodAddCourse_ShouldThrowException_WhenSQLExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        courseRepository = new CourseRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.addCourse(new Course(0, " ", " ")),
            "Error while adding student to the database");
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() throws RepositoryException {
        // when
        var allCourses = courseRepository.getAllCourses();

        // then
        assertEquals(3, allCourses.size());
    }

    @Test
    void MethodGetCourses_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        courseRepository = new CourseRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.getAllCourses(),
            "Error while adding student to the database");
    }

    @Test
    void MethodGetCourse_ById_ShouldReturnCourseFromDb() throws RepositoryException {
        // given
        var course = Course.builder()
            .courseId(1)
            .courseName("Course 1")
            .courseDescription("Some description for course 1")
            .build();

        // when
        var courseFromDb = courseRepository.getCourseById(1);

        // then
        assertEquals(course, courseFromDb);
    }

    @Test
    void MethodGetCourse_ById_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        courseRepository = new CourseRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.getCourseById(1),
            "Error while adding student to the database");
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() throws RepositoryException {
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
                .build()
            );

        // when
        var actual = courseRepository.getCoursesByStudentId(2);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        courseRepository = new CourseRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.getCoursesByStudentId(1),
            "Error while adding student to the database");
    }
}
