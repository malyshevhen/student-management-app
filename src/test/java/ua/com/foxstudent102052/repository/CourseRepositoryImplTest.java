package ua.com.foxstudent102052.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.model.Course;

public class CourseRepositoryImplTest {
    private DAOFactory daoFactory;
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() throws IOException {
        daoFactory = DAOFactoryImpl.getInstance();
        courseRepository = new CourseRepositoryImpl(daoFactory);
        daoFactory.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        daoFactory.setLogin("sa");
        daoFactory.setPassword("sa");
        daoFactory.executeSqlScript("src/test/resources/testDB.sql");
    }

    @Test
    void MethodAddCourseShouldAddCourseToDb() throws RepositoryException {
        // given
        var course = Course.builder().courseName("Course 1").build();

        // when
        courseRepository.addCourse(course);

        // then
        var allCourses = courseRepository.getAllCourses();

        assertEquals(1, allCourses.size());
    }

    @Test
    void MethodAddCourseShouldThrowExceptionWhenDAOExceptionThrown() throws RepositoryException, DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        courseRepository = new CourseRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).doPost(anyString());

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.addCourse(new Course()),
                "Course wasn`t added");
    }

    @Test
    void MethodGetAllCoursesShouldReturnListOfAllCoursesFromDB() throws RepositoryException {
        // given
        var courses = List.of(
                Course.builder().courseId(1).courseName("Course 1").courseDescription("Some course").build(),
                Course.builder().courseId(2).courseName("Course 2").courseDescription("Some course").build(),
                Course.builder().courseId(3).courseName("Course 3").courseDescription("Some course").build());

        courses.forEach(
                course -> {

                    try {
                        courseRepository.addCourse(course);
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                });

        // when
        var allCourses = courseRepository.getAllCourses();

        // then
        assertEquals(courses, allCourses);
    }

    @Test
    void MethodGetCourseByIdShouldReturnCourseFromDb() throws RepositoryException {
        // given
        var course = Course.builder().courseId(1).courseName("Course 1").courseDescription("Some course").build();
        courseRepository.addCourse(course);

        // when
        var courseFromDb = courseRepository.getCourseById(1);

        // then
        assertEquals(course, courseFromDb);
    }

    @Test
    void MethodGetCourseByIdShouldThrowExceptionWhenDAOExceptionThrown() throws RepositoryException, DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        courseRepository = new CourseRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getCourse(anyString());

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.getCourseById(1),
                "Course wasn`t added");
    }

    @Test
    void MethodGetCoursesByStudentIdShouldReturnCourseFromDb() throws DAOException, RepositoryException {
        // given
        var expected = Course.builder().courseId(1).courseName("Course 1").courseDescription("Some course").build();
        daoFactory.doPost(
                """
                        INSERT INTO courses (course_id, course_name, course_description)
                        VALUES (1, 'Course 1', 'Some course');
                        INSERT INTO students (student_id, first_name, last_name)
                        VALUES (1, 'Jar Jar', 'Binks');
                        INSERT INTO students_courses (student_id, course_id)
                        VALUES (1, 1);
                        """);

        // when
        var actual = courseRepository.getCoursesByStudentId(1);

        // then
        assertEquals(List.of(expected), actual);
    }

    @Test
    void MethodGetCoursesByStudentIdShouldThrowExceptionWhenDAOExceptionThrown()
            throws RepositoryException, DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        courseRepository = new CourseRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getCourses(anyString());

        // then
        assertThrows(RepositoryException.class, () -> courseRepository.getCoursesByStudentId(1),
                "Course wasn`t added");
    }

    @Test
    void MethodGetLastCourseShouldReturnLastCourseFromDb() throws RepositoryException {
        // given
        var expected = Course.builder().courseName("Course 1").courseDescription("Some course").build();
        List.of(
                Course.builder().courseName("Course 2").courseDescription("Some course").build(),
                Course.builder().courseName("Course 3").courseDescription("Some course").build(),
                expected).forEach(
                        course -> {

                            try {
                                courseRepository.addCourse(course);
                            } catch (RepositoryException e) {
                                e.printStackTrace();
                            }
                        });

        courseRepository.addCourse(expected);

        // when
        var courseFromDb = courseRepository.getLastCourse();

        // then
        assertEquals(expected, courseFromDb);
    }
}
