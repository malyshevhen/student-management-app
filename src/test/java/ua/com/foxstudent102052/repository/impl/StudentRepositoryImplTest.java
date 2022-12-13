package ua.com.foxstudent102052.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.datasource.impl.H2CustomDataSource;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.repository.interfaces.StudentRepository;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@Slf4j
class StudentRepositoryImplTest {

    private ua.com.foxstudent102052.repository.interfaces.PostDAO daoFactory;
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        daoFactory = new PostDAOImpl(H2CustomDataSource.getInstance());
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());
        var query = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        daoFactory.doPost(query);
    }

    @Test
    void MethodAddStudent_ShouldAddStudentToDb() {
        try {
            var expected = Student.builder()
                    .studentId(1)
                    .firstName("John")
                    .lastName("Doe")
                    .groupId(1)
                    .build();

            studentRepository.addStudent(expected);
            Student actual = studentRepository.getStudentById(1);

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Student wasn`t added");
        }
    }

    @Test
    void MethodAddStudent_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(PostDAO.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(daoFactory).doPost(anyString());

        // then
        assertThrows(RepositoryException.class, () -> studentRepository.addStudent(new Student()),
                "Error while adding student to the database");
    }

    @Test
    void MethodAddStudentToCourse_ShouldAddStudentToNewCourse() {
        try {
            var courseRepository = new CourseRepositoryImpl(daoFactory);
            var expected = Student.builder()
                    .studentId(1)
                    .firstName("John")
                    .lastName("Doe")
                    .groupId(1)
                    .build();

            var course = Course.builder()
                    .courseId(1)
                    .courseName("Java")
                    .courseDescription("Java course")
                    .build();

            studentRepository.addStudent(expected);
            courseRepository.addCourse(course);
            studentRepository.addStudentToCourse(1, 1);

            var actual = studentRepository.getStudentsByCourseId(1).get(0);

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Student wasn`t added to course");
        }
    }

    @Test
    void MethodAddStudentToCourse_ShouldThrowAnException_IfStudentIdIsInvalid() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.addStudentToCourse(2, 1),
                "Expected addStudentToCourse() to throw, but it didn't");

        assertEquals("Student with id 2 was not added to Course with id 1", thrown.getMessage());

    }

    @Test
    void MethodGetAllStudents_ShouldReturnAllStudents() {
        try {
            var expected = List.of(
                    Student.builder()
                            .studentId(1)
                            .firstName("John")
                            .lastName("Doe")
                            .groupId(1)
                            .build());

            expected.forEach(student -> {

                try {
                    studentRepository.addStudent(student);
                } catch (RepositoryException e) {
                    log.error("Error", e);
                }
            });

            var actual = studentRepository.getAllStudents();

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Students weren`t added");
        }

    }

    @Test
    void MethodGetAllStudents_ShouldThrowAnException_IfThereAreNoStudents() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.getAllStudents(),
                "Expected getAllStudents() to throw, but it didn't");

        assertEquals("There are no students in the database", thrown.getMessage());
    }

    @Test
    void MethodGetStudentsByCourseId_ShouldReturnStudentByCourseId() {
        try {
            var courseRepository = new CourseRepositoryImpl(daoFactory);
            var expected = List.of(
                    Student.builder()
                            .studentId(1)
                            .firstName("John")
                            .lastName("Doe")
                            .groupId(1)
                            .build());

            var course = Course.builder()
                    .courseId(1)
                    .courseName("Java")
                    .courseDescription("Java course")
                    .build();

            expected.forEach(student -> {

                try {
                    studentRepository.addStudent(student);
                } catch (RepositoryException e) {
                    log.error("Error", e);
                }
            });

            courseRepository.addCourse(course);
            studentRepository.addStudentToCourse(1, 1);

            var actual = studentRepository.getStudentsByCourseId(1);

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Students weren`t added");
        }
    }

    @Test
    void MethodGetStudentByCourseId_ShouldThrowAnException_IfCourseIdIsInvalid() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.getStudentsByCourseId(2),
                "Expected getStudentsByCourseId() to throw, but it didn't");

        assertEquals("Error while getting students by course id", thrown.getMessage());
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents_ByStudentNameAndCourseId() {
        try {
            var courseRepository = new CourseRepositoryImpl(daoFactory);
            var expected = List.of(
                    Student.builder()
                            .studentId(1)
                            .firstName("John")
                            .lastName("Doe")
                            .groupId(1)
                            .build());

            var course = Course.builder()
                    .courseId(1)
                    .courseName("Java")
                    .courseDescription("Java course")
                    .build();

            expected.forEach(student -> {

                try {
                    studentRepository.addStudent(student);
                } catch (RepositoryException e) {
                    log.error("Error", e);
                }
            });

            courseRepository.addCourse(course);
            studentRepository.addStudentToCourse(1, 1);

            var actual = studentRepository.getStudentsByNameAndCourse("John", 1);

            assertEquals(expected, actual);

        } catch (RepositoryException e) {
            log.error("Error", e);

            fail("Students weren`t added");
        }
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
                        log.info("Student {} was added to database", student.getStudentId());
                    } catch (RepositoryException e) {
                        log.error("Error", e);
                    }
                });
        daoFactory.doPost(
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
    void MethodRemoveStudent_ShouldThrowAnException_IfDAOExceptionWasThrown() throws DAOException {
        // given
        daoFactory = mock(PostDAO.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(daoFactory).doPost(anyString());

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
                        log.info("Student {} was added to database", student.getStudentId());
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
                        log.info("Student {} was added to database", student.getStudentId());
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
    void MethodGetStudentById_ShouldThrowException_IfDAOExceptionWasThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(daoFactory).getStudents(anyString());

        // then
        assertThrows(RepositoryException.class, () -> studentRepository.getStudentById(1),
                "Error while getting student by id");
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldRemoveStudentCourseRelation_IfExist() {
        try {
            var courseRepository = new CourseRepositoryImpl(daoFactory);
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
    void MethodRemoveStudentFromCourse_ShouldThrowAnException_IfDAOExceptionWasThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        studentRepository = new StudentRepositoryImpl(H2CustomDataSource.getInstance());

        // when
        doThrow(DAOException.class).when(daoFactory).doPost(anyString());

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
