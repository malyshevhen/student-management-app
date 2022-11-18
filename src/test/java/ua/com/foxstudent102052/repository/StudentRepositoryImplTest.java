package ua.com.foxstudent102052.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

@Slf4j
public class StudentRepositoryImplTest {

    private DAOFactory daoFactory;
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() throws IOException {
        daoFactory = DAOFactoryImpl.getInstance();
        studentRepository = new StudentRepositoryImpl(daoFactory);
        daoFactory.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        daoFactory.setLogin("sa");
        daoFactory.setPassword("sa");
        daoFactory.executeSqlScript("src/test/resources/testDB.sql");
    }

    @Test
    void MethodAddStudentShouldReturnSameStudentAsAdded() {
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

            assertFalse(true);
        }
    }

    @Test
    void MethodAddStudentToCourseShouldAddStudentToNewCourse() {
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

            assertFalse(true);
        }
    }

    @Test
    void MethodAddStudentToCourseShouldThrowAnExceptionIfStudentIdIsInvalid() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.addStudentToCourse(2, 1),
                "Expected addStudentToCourse() to throw, but it didn't");

        assertEquals("Student with id 2 was not added to Course with id 1", thrown.getMessage());

    }

    @Test
    void MethodGetAllStudentsShouldReturnAllStudents() {
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

            assertFalse(true);
        }

    }

    @Test
    void MethodGetAllStudentsShouldThrowAnExceptionIfThereAreNoStudents() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.getAllStudents(),
                "Expected getAllStudents() to throw, but it didn't");

        assertEquals("There are no students in the database", thrown.getMessage());
    }

    @Test
    void MethodGetStudentsByCourseIdShouldReturnStudentByCourseId() {
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

            assertFalse(true);
        }
    }

    @Test
    void MethodGetStudentByCourseIdShouldThrowAnExceptionIfCourseIdIsInvalid() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.getStudentsByCourseId(2),
                "Expected getStudentsByCourseId() to throw, but it didn't");

        assertEquals("Error while getting students by course id", thrown.getMessage());
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldReturnListOfStudentsByStudentNameAndCourseId() {
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

            assertFalse(true);
        }
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldThrowAnExceptionIfNoRelationFromThem() {

        RepositoryException thrown = assertThrows(
                RepositoryException.class,
                () -> studentRepository.getStudentsByNameAndCourse("John", 2),
                "Expected getStudentsByNameAndCourse() to throw, but it didn't");

        assertEquals("Error while getting students by name and course", thrown.getMessage());
    }

    @Test
    void MethodShouldRemoveStudentIfItInDataBase() throws RepositoryException {
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

        studentRepository.removeStudent(1);

        int expected = 1;
        int actual = 0;

        actual = studentRepository.getAllStudents().size();

        assertEquals(expected, actual);
    }

    @Test
    void MethodRemoveStudentFromCourseShouldRemoveStudentCourseRelationIfExist() {
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

            assertFalse(true);
        }
    }

    @Test
    void MethodGetLastStudentShouldReturnLastAddedStudent() {
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

            assertFalse(true);
        }
    }
}
