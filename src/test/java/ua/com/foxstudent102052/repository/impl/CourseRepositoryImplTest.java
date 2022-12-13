package ua.com.foxstudent102052.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.datasource.impl.H2CustomDataSource;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.CourseRepository;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseRepositoryImplTest {
    private PostDAO daoFactory;
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        H2CustomDataSource dataSource = H2CustomDataSource.getInstance();
        daoFactory = new PostDAOImpl(dataSource);
        courseRepository = new CourseRepositoryImpl(dataSource);
        var query = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        daoFactory.doPost(query);
    }

    @Test
    void MethodAddCourse_ShouldAddCourseToDb() throws RepositoryException {
        // given
        var course = Course.builder().courseName("Course 1").build();

        // when
        courseRepository.addCourse(course);

        // then
        var allCourses = courseRepository.getAllCourses();

        assertEquals(1, allCourses.size());
    }

    @Disabled
    @Test
    void MethodAddCourse_ShouldThrowException_WhenSQLExceptionThrown() {
        // todo: write test;
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() throws RepositoryException {
        // given
        var courses = List.of(
            Course.builder().courseId(1).courseName("Course 1").courseDescription("Some course").build(),
            Course.builder().courseId(2).courseName("Course 2").courseDescription("Some course").build(),
            Course.builder().courseId(3).courseName("Course 3").courseDescription("Some course").build());

        courses.forEach(course -> {
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

    @Disabled
    @Test
    void MethodGetCourses_ShouldThrowException_WhenDAOExceptionThrown() {
        // todo: write test;
    }

    @Test
    void MethodGetCourse_ById_ShouldReturnCourseFromDb() throws RepositoryException {
        // given
        var course = Course.builder().courseId(1).courseName("Course 1").courseDescription("Some course").build();
        courseRepository.addCourse(course);

        // when
        var courseFromDb = courseRepository.getCourseById(1);

        // then
        assertEquals(course, courseFromDb);
    }

    @Disabled
    @Test
    void MethodGetCourse_ById_ShouldThrowException_WhenDAOExceptionThrown() {
        // todo: write test;
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() throws DAOException, RepositoryException {
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

    @Disabled
    @Test
    void MethodGetCourses_ByStudentId_ShouldThrowException_WhenDAOExceptionThrown() {
        // todo: write test;
    }

    @Test
    void MethodGetLastCourse_ShouldReturnLastCourseFromDb() throws RepositoryException {
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

    @Disabled
    @Test
    void MethodGetLastCourse_ShouldThrowException_WhenDAOExceptionThrown() {
        // todo: write test;
    }
}
