package ua.com.foxstudent102052.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentRepositoryImplTest {
    DAOFactory daoFactory;
    StudentRepository studentRepository;
    CourseRepository courseRepository;
    
    @BeforeEach
    void setUp() {
        daoFactory = new DAOFactoryImpl();
        daoFactory.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        daoFactory.setLogin("sa");
        daoFactory.setPassword("sa");
        
        studentRepository = new StudentRepositoryImpl(daoFactory);
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
    void canAddStudent() {
        int id = 1;
        var expected = new Student(id,1, "Darth", "Vader");
        studentRepository.addStudent(expected);
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canRemoveStudent() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader')
            """);
        int id = 1;
        
        studentRepository.removeStudent(id);
        
        var expected = new Student();
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canUpdateStudentFirstName() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader')
            """);
        int id = 1;
        studentRepository.updateStudentFirstName(id, "Anakin");
        
        var expected = new Student(id,1, "Anakin", "Vader");
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canUpdateStudentLastName() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader')
            """);
        int id = 1;
        studentRepository.updateStudentLastName(id, "Skywalker");
        
        var expected = new Student(id,1, "Darth", "Skywalker");
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canUpdateStudentGroup() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader')
            """);
        int id = 1;
        studentRepository.updateStudentGroup(id, 2);
        
        var expected = new Student(id,2, "Darth", "Vader");
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canUpdateStudent() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Anakin', 'Skywalker')
            """);
        int id = 1;
        var expected = new Student(id,2, "Dart", "Vader");
        studentRepository.updateStudent(expected);
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canAddStudentToCourse() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader');

            INSERT INTO courses (course_name, course_description)
            VALUES ('Java', 'Java course');
            """);
        int studentId = 1;
        int courseId = 1;
        
        studentRepository.addStudentToCourse(studentId, courseId);
        
        var expected = new ArrayList<Student>();
        expected.add(new Student(studentId,1, "Darth", "Vader"));

        var actual = courseRepository.getStudentsByCourseId(courseId);
        
        assertEquals(expected, actual);
    }

    @Test
    void canRemoveStudentFromCourse() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader')

            INSERT INTO courses (course_name, course_description)
            VALUES ('Java', 'Java course')

            INSERT INTO students_courses (student_id, course_id)
            VALUES (1, 1)
            """);
        int studentId = 1;
        int courseId = 1;
        
        studentRepository.removeStudentFromCourse(studentId, courseId);
        
        var expected = List.of();
        
        var actual = courseRepository.getStudentsByCourseId(courseId);
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentById() {
        daoFactory.doPost("""
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader')
            """);
        int id = 1;
        
        var expected = new Student(id,1, "Darth", "Vader");
        
        var actual = studentRepository.getStudentById(id);
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetAllStudents() {
        daoFactory.doPost(
            """
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader');
            
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (2, 'Luke', 'Skywalker');
            """);
        
        var expected = new ArrayList<Student>();
        expected.add(new Student(1,1, "Darth", "Vader"));
        expected.add(new Student(2,2, "Luke", "Skywalker"));
        
        var actual = studentRepository.getAllStudents();
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsByFirstName() {
        daoFactory.doPost(
            """
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader');
            
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (2, 'Luke', 'Skywalker');
            """);
        
        var expected = new ArrayList<Student>();
        expected.add(new Student(1,1, "Darth", "Vader"));
        
        var actual = studentRepository.getStudentsByFirstName("Darth");
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsByLastName() {
        daoFactory.doPost(
            """
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader');
            
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (2, 'Luke', 'Skywalker');
            """);
        
        var expected = new ArrayList<Student>();
        expected.add(new Student(2,2, "Luke", "Skywalker"));
        
        var actual = studentRepository.getStudentsByLastName("Skywalker");
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsByFullName() {
        daoFactory.doPost(
            """
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader');
            
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (2, 'Luke', 'Skywalker');
            """);
        
        var expected = new ArrayList<Student>();
        expected.add(new Student(1,1, "Darth", "Vader"));
        
        var actual = studentRepository.getStudentsByFullName("Darth", "Vader");
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetCoursesByStudentId() {
        daoFactory.doPost(
            """
            INSERT INTO students (group_id, first_name, last_name)
            VALUES (1, 'Darth', 'Vader');
            
            INSERT INTO courses (course_name, course_description)
            VALUES ('Java', 'Java course');
            
            INSERT INTO courses (course_name, course_description)
            VALUES ('C#', 'C# course');
            
            INSERT INTO students_courses (student_id, course_id)
            VALUES (1, 1);
            
            INSERT INTO students_courses (student_id, course_id)
            VALUES (1, 2);
            """);
        
        var expected = new ArrayList<Course>();
        expected.add(new Course(1, "Java", "Java course"));
        expected.add(new Course(2, "C#", "C# course"));
        
        var actual = studentRepository.getCoursesByStudentId(1);
        
        assertEquals(expected, actual);
    }
}
