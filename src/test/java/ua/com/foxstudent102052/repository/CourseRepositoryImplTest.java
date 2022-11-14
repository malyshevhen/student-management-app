package ua.com.foxstudent102052.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseRepositoryImplTest {
    private DAOFactory daoFactory;
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        daoFactory = new DAOFactoryImpl();
        daoFactory.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        daoFactory.setLogin("sa");
        daoFactory.setPassword("sa");

        courseRepository = new CourseRepositoryImpl(daoFactory);

        daoFactory.doPost("CREATE TABLE IF NOT EXISTS students (" +
            "student_id INT NOT NULL AUTO_INCREMENT, " +
            "group_id INT, " +
            "first_name VARCHAR(225), " +
            "last_name VARCHAR(225), " +
            "PRIMARY KEY (student_id))");

        daoFactory.doPost("CREATE TABLE IF NOT EXISTS courses (" +
            "course_id INT NOT NULL AUTO_INCREMENT, " +
            "course_name VARCHAR(255), " +
            "course_description VARCHAR(255), " +
            "PRIMARY KEY (course_id))");

        daoFactory.doPost("CREATE TABLE IF NOT EXISTS groups (" +
            "group_id INT NOT NULL AUTO_INCREMENT, " +
            "group_name VARCHAR(255), " +
            "PRIMARY KEY (group_id))");

        daoFactory.doPost("CREATE TABLE IF NOT EXISTS students_courses (" +
            "student_id INT NOT NULL, " +
            "course_id INT NOT NULL, " +
            "PRIMARY KEY (student_id, course_id), " +
            "FOREIGN KEY (student_id) REFERENCES students(student_id), " +
            "FOREIGN KEY (course_id) REFERENCES courses(course_id))");
    }
    
    @AfterEach
    void tearDown() {
        daoFactory.doPost("DROP TABLE students_courses");
        daoFactory.doPost("DROP TABLE students");
        daoFactory.doPost("DROP TABLE courses");
        daoFactory.doPost("DROP TABLE groups");
    }

    @Test
    void canAddCourse() {
        var expected = new Course(1, "Java", "Java expected");

        courseRepository.addCourse(expected);

        var actual = courseRepository.getCourseById(1);

        assertEquals(expected, actual);
    }

    @Test
    void canRemoveCourse() {
        var courseToDb = new Course(1, "Java", "Java expected");

        courseRepository.addCourse(courseToDb);
        courseRepository.removeCourse(1);

        var actual = courseRepository.getCourseById(1);
        var expected = new Course(0, null, null);

        assertEquals(expected, actual);
    }

    @Test
    void canUpdateCourseName() {
        var courseToDb = new Course(1, "Java", "Java expected");

        courseRepository.addCourse(courseToDb);
        courseRepository.updateCourseName(1, "Java updated");

        var actual = courseRepository.getCourseById(1);
        var expected = new Course(1, "Java updated", "Java expected");

        assertEquals(expected, actual);
    }

    @Test
    void canUpdateCourseDescription() {
        var courseToDb = new Course(1, "Java", "Java expected");

        courseRepository.addCourse(courseToDb);
        courseRepository.updateCourseDescription(1, "Java updated");

        var actual = courseRepository.getCourseById(1);
        var expected = new Course(1, "Java", "Java updated");

        assertEquals(expected, actual);
    }

    @Test
    void canUpdateCourse() {
        var courseToDb = new Course(1, "Java", "Java expected");
        var expected = new Course(1, "Java updated", "Java updated");

        courseRepository.addCourse(courseToDb);
        courseRepository.updateCourse(expected);

        var actual = courseRepository.getCourseById(1);

        assertEquals(expected, actual);
    }

    @Test
    void canGetAllCourses() {
        var expected = new ArrayList<Course>();
        expected.add(new Course(1, "Java", "Java expected"));
        expected.add(new Course(2, "JavaScript", "JavaScript expected"));
        expected.add(new Course(3, "Python", "Python expected"));
        expected.add(new Course(4, "C#", "C# expected"));

        for (var course : expected) {
            courseRepository.addCourse(course);
        }

        var actual = courseRepository.getAllCourses();

        assertEquals(expected, actual);
    }

    @Test
    void canGetCourseById() {
        var expected = new Course(1, "Java", "Java expected");

        courseRepository.addCourse(expected);

        var actual = courseRepository.getCourseById(1);

        assertEquals(expected, actual);
    }

    @Test
    void canGetCourseByName() {
        var expected = new Course(1, "Java", "Java expected");
        daoFactory.doPost(
            """
                INSERT INTO courses (course_id, course_name, course_description)
                VALUES (1, 'Java', 'Java expected');
                """
        );

        var actual = courseRepository.getCourseByName("Java");

        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsByCourseId() {
        daoFactory.doPost(
            """
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Han', 'Solo');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Luke', 'Skywalker');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Leia', 'Organa');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Chewbacca', 'Wookie');
                
                INSERT INTO courses (course_name, course_description)
                VALUES ('Java', 'Java expected');
                
                INSERT INTO courses (course_name, course_description)
                VALUES ('JavaScript', 'JavaScript expected');
                
                INSERT INTO students_courses (student_id, course_id)
                VALUES (1, 1);
                
                INSERT INTO students_courses (student_id, course_id)
                VALUES (2, 1);
                
                INSERT INTO students_courses (student_id, course_id)
                VALUES (3, 1);
                
                INSERT INTO students_courses (student_id, course_id)
                VALUES (4, 2);
                """
        );

        var courseId = 1;

        var expected = new ArrayList<Student>();
        expected.add(new Student(1, 1, "Han", "Solo"));
        expected.add(new Student(2, 1, "Luke", "Skywalker"));
        expected.add(new Student(3, 1, "Leia", "Organa"));

        var actual = courseRepository.getStudentsByCourseId(courseId);

        assertEquals(expected, actual);
    }
}
