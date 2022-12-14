package ua.com.foxstudent102052.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.datasource.impl.H2CustomDataSource;
import ua.com.foxstudent102052.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.repository.interfaces.StudentRepository;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@Slf4j
class StudentRepositoryImplTest {
    private CustomDataSource customDataSource;
    private StudentRepository studentRepository;
    private PostDAO postDAO;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        customDataSource = H2CustomDataSource.getInstance();
        studentRepository = new StudentRepositoryImpl(customDataSource);
        postDAO = new PostDAOImpl(customDataSource);
        var ddlScript = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        var dmlScript = FileUtils.readTextFile("src/test/resources/scripts/dml/testDB_Data.sql");
        postDAO.doPost(ddlScript);
        postDAO.doPost(dmlScript);
    }

    @Test
    void MethodAddStudent_ShouldAddStudentToDb() throws RepositoryException {
        // given
        var expected = Student.builder()
            .studentId(1)
            .groupId(1)
            .firstName("John")
            .lastName("Doe")
            .build();

        // when
        studentRepository.addStudent(expected);
        var actual = studentRepository.getStudentById(11);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodAddStudent_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> studentRepository.addStudent(new Student(0, 0, "", "")),
            "Error while adding student to the database");
    }

    @Test
    void MethodAddStudentToCourse_ShouldAddStudentToNewCourse() throws RepositoryException {
        // given
        var expected = Student.builder()
            .studentId(1)
            .groupId(1)
            .firstName("Leia")
            .lastName("Organa")
            .build();

        // when
        studentRepository.addStudentToCourse(1, 2);
        var actual = studentRepository.getStudentsByCourseId(1).get(0);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodAddStudentToCourse_ShouldThrowAnException_IfStudentIdIsInvalid() {
        // when
        RepositoryException thrown = assertThrows(
            RepositoryException.class,
            () -> studentRepository.addStudentToCourse(12, 1),
            "Expected addStudentToCourse() to throw, but it didn't");

        // then
        assertEquals("Student with id 12 was not added to Course with id 1", thrown.getMessage());

    }

    @Test
    void MethodGetAllStudents_ShouldReturnAllStudents() throws RepositoryException {
        // when
        var actual = studentRepository.getAllStudents().size();

        // then
        assertEquals(10, actual);
    }

    @Test
    void MethodGetStudentsByCourseId_ShouldReturnStudentByCourseId() throws RepositoryException {
        var expected = List.of(
            Student.builder()
                .studentId(1)
                .groupId(1)
                .firstName("Leia")
                .lastName("Organa")
                .build(),
            Student.builder()
                .studentId(2)
                .groupId(1)
                .firstName("Luke")
                .lastName("Skywalker")
                .build(),
            Student.builder()
                .studentId(4)
                .groupId(1)
                .firstName("Padme")
                .lastName("Amidala")
                .build(),
            Student.builder()
                .studentId(5)
                .groupId(2)
                .firstName("Dart")
                .lastName("Maul")
                .build(),
            Student.builder()
                .studentId(9)
                .groupId(2)
                .firstName("Dart")
                .lastName("Vader")
                .build(),
            Student.builder()
                .studentId(10)
                .groupId(3)
                .firstName("Jah Jah")
                .lastName("Binks")
                .build()
        );

        var actual = studentRepository.getStudentsByCourseId(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentByCourseId_ShouldThrowAnException_IfCourseIdIsInvalid() {

        RepositoryException thrown = assertThrows(
            RepositoryException.class,
            () -> studentRepository.getStudentsByCourseId(5),
            "Expected getStudentsByCourseId() to throw, but it didn't");

        assertEquals("Error while getting students by course id", thrown.getMessage());
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents_ByStudentNameAndCourseId() throws RepositoryException {
        var expected = List.of(
            Student.builder()
                .studentId(5)
                .groupId(2)
                .firstName("Dart")
                .lastName("Maul")
                .build(),
            Student.builder()
                .studentId(9)
                .groupId(2)
                .firstName("Dart")
                .lastName("Vader")
                .build()
            );

        var actual = studentRepository.getStudentsByNameAndCourse("Dart", 1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldThrowAnException_IfNoRelationFromThem() {

        RepositoryException thrown = assertThrows(
            RepositoryException.class,
            () -> studentRepository.getStudentsByNameAndCourse("John", 2),
            "Expected getStudentsByNameAndCourse() to throw, but it didn't");

        assertEquals("Error while getting students by name and course", thrown.getMessage());
    }

    @Test
    void MethodRemoveStudent_ShouldRemoveStudent_IfItInDataBase() throws RepositoryException, DAOException {
        List.of(
                Student.builder()
                    .studentId(1)
                    .firstName("John")
                    .lastName("Doe")
                    .groupId(1)
                    .build(),
                Student.builder()
                    .studentId(2)
                    .firstName("David")
                    .lastName("Black")
                    .groupId(1)
                    .build())
            .forEach(student -> {

                try {
                    studentRepository.addStudent(student);
                    log.info("Student {} was added to database", student.studentId());
                } catch (RepositoryException e) {
                    log.error("Error", e);
                }
            });
        postDAO.doPost(
            """
                INSERT INTO courses (course_name, course_description)
                VALUES ('Java', 'Java course');
                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                """
        );

        studentRepository.removeStudent(1);

        int expected = 1;
        int actual;

        actual = studentRepository.getAllStudents().size();

        assertEquals(expected, actual);
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfDAOExceptionWasThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(customDataSource).getConnection();

        // then
        assertThrows(
            RepositoryException.class,
            () -> studentRepository.removeStudent(1),
            "Expected removeStudent() to throw, but it didn't");
    }

    @Test
    void MethodGetStudentById_ShouldReturnStudentFromDb() throws RepositoryException {
        List.of(
                Student.builder()
                    .studentId(1)
                    .firstName("John")
                    .lastName("Doe")
                    .groupId(1)
                    .build(),
                Student.builder()
                    .studentId(2)
                    .firstName("David")
                    .lastName("Black")
                    .groupId(1)
                    .build())
            .forEach(student -> {

                try {
                    studentRepository.addStudent(student);
                    log.info("Student {} was added to database", student.studentId());
                } catch (RepositoryException e) {
                    log.error("Error", e);
                }
            });

        var expected = Student.builder()
            .studentId(1)
            .firstName("John")
            .lastName("Doe")
            .groupId(1)
            .build();

        var actual = studentRepository.getStudentById(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetStudentById_ShouldThrowException_WhenStudentDoseNotExist() {
        List.of(
                Student.builder()
                    .studentId(1)
                    .firstName("John")
                    .lastName("Doe")
                    .groupId(1)
                    .build(),
                Student.builder()
                    .studentId(2)
                    .firstName("David")
                    .lastName("Black")
                    .groupId(1)
                    .build())
            .forEach(student -> {

                try {
                    studentRepository.addStudent(student);
                    log.info("Student {} was added to database", student.studentId());
                } catch (RepositoryException e) {
                    log.error("Error", e);
                }
            });

        assertThrows(
            RepositoryException.class,
            () -> studentRepository.getStudentById(3),
            "Expected getStudentById() to throw, but it didn't");
    }

    @Test
    void MethodGetStudentById_ShouldThrowException_IfDAOExceptionWasThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> studentRepository.getStudentById(1),
            "Error while getting student by id");
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldRemoveStudentCourseRelation_IfExist() {
        try {
            var courseRepository = new CourseRepositoryImpl(customDataSource);
            var course = Course.builder()
                .courseId(1)
                .courseName("Java")
                .courseDescription("Java course")
                .build();

            courseRepository.addCourse(course);

            List.of(
                    Student.builder()
                        .studentId(1)
                        .firstName("John")
                        .lastName("Doe")
                        .groupId(1)
                        .build(),
                    Student.builder()
                        .studentId(2)
                        .firstName("David")
                        .lastName("Black")
                        .groupId(1)
                        .build())
                .forEach(student -> {

                    try {
                        studentRepository.addStudent(student);
                    } catch (RepositoryException e) {
                        log.error("Error", e);
                    }
                });

            studentRepository.addStudentToCourse(1, 1);
            studentRepository.addStudentToCourse(2, 1);

            studentRepository.removeStudentFromCourse(1, 1);

            int expected = 1;

            int actual = studentRepository.getStudentsByCourseId(1).size();

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Students weren`t added");
        }
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldThrowAnException_IfDAOExceptionWasThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class,
            () -> studentRepository.removeStudentFromCourse(1, 1),
            "Expected removeStudentFromCourse() to throw, but it didn't");
    }

    @Test
    void MethodGetLastStudent_ShouldReturnLastAddedStudent() {
        try {
            var expected = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .groupId(1)
                .build();

            List.of(
                    Student.builder()
                        .firstName("David")
                        .lastName("Black")
                        .groupId(1)
                        .build(),
                    expected)
                .forEach(student -> {

                    try {
                        studentRepository.addStudent(student);
                    } catch (RepositoryException e) {
                        log.error("Error", e);
                    }
                });

            studentRepository.addStudent(expected);

            var actual = studentRepository.getLastStudent();

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Students weren`t added");
        }
    }

    @Test
    void MethodGetLastStudent_ShouldThrowAnException_IfNoStudentsInDataBase() {

        RepositoryException thrown = assertThrows(
            RepositoryException.class,
            () -> studentRepository.getLastStudent(),
            "Expected getLastStudent() to throw, but it didn't");

        assertEquals("Error while getting last student", thrown.getMessage());
    }

    @Test
    void MethodGetLastStudent_ShouldReturnStudentWithMaxIdFromDb() {
        try {
            var expected = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .groupId(1)
                .build();

            List.of(
                    Student.builder()
                        .firstName("David")
                        .lastName("Black")
                        .groupId(1)
                        .build(),
                    expected)
                .forEach(student -> {

                    try {
                        studentRepository.addStudent(student);
                    } catch (RepositoryException e) {
                        log.error("Error", e);
                    }
                });

            var actual = studentRepository.getLastStudent();

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Students weren`t added");
        }
    }
}
