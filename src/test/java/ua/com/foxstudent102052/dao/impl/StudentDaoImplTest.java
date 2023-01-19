package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.utils.FileUtils;

@JdbcTest
class StudentDaoImplTest {
    private final StudentDao studentDao;
    private final PostDAO postDAO;
    private final FileUtils fileUtils;

    @Autowired
    public StudentDaoImplTest(DataSource dataSource) {
        this.studentDao = new StudentDaoImpl(dataSource);
        this.postDAO = new PostDaoImpl(dataSource);
        this.fileUtils = new FileUtils();
    }

    @BeforeEach
    public void setUp() throws DAOException {
        var ddlScript = fileUtils.readFileFromResourcesAsString("scripts/ddl/Table_creation.sql");
        var dmlScript = fileUtils.readFileFromResourcesAsString("scripts/dml/testDB_Data.sql");

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
        var actual = studentDao.getStudentsByCourse(1).get(0);

        // then
        assertEquals(expected, actual);
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
                        .id(3)
                        .groupId(1)
                        .firstName("Han")
                        .lastName("Solo")
                        .build(),
                Student.builder()
                        .id(4)
                        .groupId(1)
                        .firstName("Padme")
                        .lastName("Amidala")
                        .build());

        var actual = studentDao.getStudentsByGroup(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByGroup_ShouldReturnStudentByGroupId() throws DAOException {
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
                        .build());

        var actual = studentDao.getStudentsByCourse(1);

        assertEquals(expected, actual);
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
                        .build());

        var actual = studentDao.getStudents("Dart", 1);

        assertEquals(expected, actual);
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
    void MethodRemoveStudentFromCourse_ShouldRemoveStudentCourseRelation_IfExist() throws DAOException {
        // given
        studentDao.removeStudentFromCourse(1, 1);
        var expected = List.of();

        // when
        var actual = studentDao.getStudents("Leia", 1);

        // then
        assertEquals(expected, actual);
    }
}
