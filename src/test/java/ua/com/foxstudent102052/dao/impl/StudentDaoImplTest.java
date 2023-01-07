package ua.com.foxstudent102052.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.utils.FileUtils;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class StudentDaoImplTest {
    private CustomDataSource customDataSource;
    private StudentDao studentDao;

    @BeforeEach
    public void setUp() throws DAOException {
        customDataSource = PooledDataSource.getInstance();
        studentDao = new StudentDaoImpl(customDataSource);

        FileUtils fileUtils = new FileUtils();
        var ddlScript = fileUtils.readFileFromResourcesAsString("scripts/ddl/testDB.sql");
        var dmlScript = fileUtils.readFileFromResourcesAsString("scripts/dml/testDB_Data.sql");

        PostDAO postDAO = new PostDAOImpl(customDataSource);
        postDAO.doPost(ddlScript);
        postDAO.doPost(dmlScript);
    }

    @Test
    void MethodAddStudent_ShouldAddStudentToDb() throws DAOException {
        // given
        var newStudent = Student.builder()
            .id(1)
            .groupId(1)
            .firstName("John")
            .lastName("Doe")
            .build();

        // when
        studentDao.addStudent(newStudent);
        int expected = studentDao.getStudents().size();
        int actual = 11;

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodAddStudent_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);
        var student = new Student(0, 0, "", "");

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> studentDao.addStudent(student),
            "Error while adding student to the database");
    }

    @Test
    void MethodAddStudentToCourse_ShouldAddStudentToNewCourse() throws DAOException {
        // given
        var expected = Student.builder()
            .id(1)
            .groupId(1)
            .firstName("Leia")
            .lastName("Organa")
            .build();

        // when
        studentDao.addStudentToCourse(1, 2);
        var actual = studentDao.getStudents(1).get(0);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodAddStudentToCourse_ShouldThrowAnException_IfIdIsInvalid() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> studentDao.addStudentToCourse(1, 1),
            "Error while adding student to the database");

    }

    @Test
    void MethodGetAllStudents_ShouldReturnAllStudents() throws DAOException {
        // when
        var actual = studentDao.getStudents().size();

        // then
        assertEquals(10, actual);
    }

    @Test
    void MethodGetStudentsByCourseId_ShouldReturnStudentByCourseId() throws DAOException {
        var expected = List.of(
            Student.builder()
                .id(1)
                .groupId(1)
                .firstName("Leia")
                .lastName("Organa")
                .build(),
            Student.builder()
                .id(2)
                .groupId(1)
                .firstName("Luke")
                .lastName("Skywalker")
                .build(),
            Student.builder()
                .id(4)
                .groupId(1)
                .firstName("Padme")
                .lastName("Amidala")
                .build(),
            Student.builder()
                .id(5)
                .groupId(2)
                .firstName("Dart")
                .lastName("Maul")
                .build(),
            Student.builder()
                .id(9)
                .groupId(2)
                .firstName("Dart")
                .lastName("Vader")
                .build(),
            Student.builder()
                .id(10)
                .groupId(3)
                .firstName("Jah Jah")
                .lastName("Binks")
                .build()
        );

        var actual = studentDao.getStudents(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentByCourseId_ShouldThrowAnException_IfCourseIdIsInvalid() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> studentDao.getStudents(1),
            "Error while adding student to the database");
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents_ByStudentNameAndCourseId() throws DAOException {
        var expected = List.of(
            Student.builder()
                .id(5)
                .groupId(2)
                .firstName("Dart")
                .lastName("Maul")
                .build(),
            Student.builder()
                .id(9)
                .groupId(2)
                .firstName("Dart")
                .lastName("Vader")
                .build()
        );

        var actual = studentDao.getStudents("Dart", 1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldThrowAnException_IfNoRelationFromThem() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> studentDao.getStudents("SomeName", 1),
            "Error while adding student to the database");
    }

    @Test
    void MethodRemoveStudent_ShouldRemoveStudent_IfItInDataBase() throws DAOException {
        // when
        studentDao.removeStudent(1);

        int expected = 9;
        int actual = studentDao.getStudents().size();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfDAOExceptionWasThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(
            DAOException.class,
            () -> studentDao.removeStudent(1),
            "Expected removeStudent() to throw, but it didn't");
    }

    @Test
    void MethodGetStudentById_ShouldReturnStudentFromDb() throws DAOException {
        // given
        var expected = Student.builder()
            .id(1)
            .groupId(1)
            .firstName("Leia")
            .lastName("Organa")
            .build();

        // when
        var actual = studentDao.getStudent(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentById_ShouldThrowException_WhenStudentDoseNotExist() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then

        assertThrows(
            DAOException.class,
            () -> studentDao.getStudent(3),
            "Expected getStudentById() to throw, but it didn't");
    }

    @Test
    void MethodGetStudentById_ShouldThrowException_IfDAOExceptionWasThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> studentDao.getStudent(1),
            "Error while getting student by id");
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldRemoveStudentCourseRelation_IfExist() throws DAOException {
        // given
        studentDao.removeStudentFromCourse(1, 1);
        var expected = List.of();

        // when
        var actual = studentDao.getStudents("Leia", 1);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldThrowAnException_IfDAOExceptionWasThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentDao = new StudentDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class,
            () -> studentDao.removeStudentFromCourse(1, 1),
            "Expected removeStudentFromCourse() to throw, but it didn't");
    }
}
