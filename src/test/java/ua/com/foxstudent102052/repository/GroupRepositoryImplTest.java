package ua.com.foxstudent102052.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupRepositoryImplTest {
    private static DAOFactory daoFactory;
    private static GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        daoFactory = new DAOFactoryImpl();
        daoFactory.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        daoFactory.setLogin("sa");
        daoFactory.setPassword("sa");

        groupRepository = new GroupRepositoryImpl(daoFactory);

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
    void canAddGroup() {
        var expected = new Group(1, "Group 1");

        groupRepository.addGroup(expected);

        var actual = groupRepository.getGroupById(expected.getGroupId());

        assertEquals(expected, actual);
    }

    @Test
    void canRemoveGroupById() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 1')
                """);
        
        groupRepository.removeGroupById(1);
        
        var expected = new Group();
        
        var actual = groupRepository.getGroupById(1);
        
        assertEquals(expected, actual);
    }

    @Test
    void canUpdateGroupById() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 1')
                """);
        
        var expected = new Group(1, "Group 2");
        groupRepository.updateGroupById(expected);
        
        var actual = groupRepository.getGroupById(1);
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetGroupById() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 1')
                """);
        
        var expected = new Group(1, "Group 1");
        
        var actual = groupRepository.getGroupById(1);
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetGroupByName() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('JV-102')
                """);
        
        var expected = new Group(1, "JV-102");
        
        var actual = groupRepository.getGroupByName("JV-102");
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetAllGroups() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 1')
                """);
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 2')
                """);
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 3')
                """);
        
        var expected = new ArrayList<Group>();
        expected.add(new Group(1, "Group 1"));
        expected.add(new Group(2, "Group 2"));
        expected.add(new Group(3, "Group 3"));
        
        var actual = groupRepository.getAllGroups();
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetGroupsSmallerThen() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 1');
                     
                INSERT INTO groups (group_name)
                VALUES ('Group 2');

                INSERT INTO groups (group_name)
                VALUES ('Group 3');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Han', 'Solo');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Luke', 'Skywalker');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (2, 'Leia', 'Organa');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (2, 'Chewbacca', 'Wookie');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (3, 'Darth', 'Vader');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (3, 'Obi-Wan', 'Kenobi');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (3, 'Yoda', 'Jedi');
                """);
        
        var expected = new ArrayList<Group>();
        expected.add(new Group(1, "Group 1"));
        expected.add(new Group(2, "Group 2"));
        
        var actual = groupRepository.getGroupsSmallerThen(2);
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsByGroup() {
        daoFactory.doPost(
            """
                INSERT INTO groups (group_name)
                VALUES ('Group 1');
                     
                INSERT INTO groups (group_name)
                VALUES ('Group 2');

                INSERT INTO groups (group_name)
                VALUES ('Group 3');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Han', 'Solo');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (1, 'Luke', 'Skywalker');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (2, 'Leia', 'Organa');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (2, 'Chewbacca', 'Wookie');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (3, 'Darth', 'Vader');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (3, 'Obi-Wan', 'Kenobi');
                
                INSERT INTO students (group_id, first_name, last_name)
                VALUES (3, 'Yoda', 'Jedi');
                """);
        
        var expected = new ArrayList<Student>();
        expected.add(new Student(1, 1, "Han", "Solo"));
        expected.add(new Student(2, 1, "Luke", "Skywalker"));
        
        var actual = groupRepository.getStudentsByGroup(1);
        
        assertEquals(expected, actual);
    }
}
