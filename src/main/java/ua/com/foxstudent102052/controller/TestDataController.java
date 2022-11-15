package ua.com.foxstudent102052.controller;

import ua.com.foxstudent102052.repository.*;
import ua.com.foxstudent102052.service.*;

public class TestDataController {
    DAOFactory daoFactory = new DAOFactoryImpl();

    StudentRepository studentRepository = new StudentRepositoryImpl(daoFactory);
    CourseRepository courseRepository = new CourseRepositoryImpl(daoFactory);
    GroupRepository groupRepository = new GroupRepositoryImpl(daoFactory);
    StudentService studentService = new StudentServiceImpl(studentRepository);
    CourseService courseService = new CourseServiceImpl(courseRepository);
    GroupService groupService = new GroupServiceImpl(groupRepository);

    String[] firstNames = {
        "John", "Jack", "Bob", "Mike", "Tom",
        "Alex", "Nick", "Sam", "Bill", "George",
        "Kate", "Liza", "Ann", "Jane", "Marry",
        "Linda", "Sara", "Kate", "Liza", "Ann"
    };
    String[] lastNames = {
        "Smith", "Johnson", "Williams", "Brown", "Jones",
        "Miller", "Davis", "Garcia", "Rodriguez", "Wilson",
        "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez",
        "Moore", "Martin", "Jackson", "Thompson", "White"
    };
    String[] groupNames = {
        "GR-001", "GR-002", "GR-003", "GR-004", "GR-005",
        "GR-006", "GR-007", "GR-008", "GR-009", "GR-010"
    };
    String[] courseNames = {
        "Mathematics", "Biology", "Chemistry", "Physics", "English",
        "History", "Geography", "Literature", "Art", "Music"
    };
    String[] courseDescriptions = {
        """
            Mathematics, the science of structure, order,
             and relation that has evolved from counting,
             measuring, and describing the shapes of objects.
            """,
        """
            Biology is the natural science that studies life and living organisms,
             including their physical structure, chemical processes,
             molecular interactions, physiological mechanisms, development and evolution.
            """,
        """
            Chemistry is a branch of physical science that studies the composition,
             structure, properties and change of matter.
            """,
        """
            Physics is the natural science that involves the study of matter and its
             motion through space and time,
             along with related concepts such as energy and force.
            """,
        """
            English is a West Germanic language that was first spoken in early
             medieval England and eventually became a global lingua franca.
            """,
        """
            History is the study of the past as it is described in written documents.
             Events occurring before written record are considered prehistory.
            """,
        """
            Geography is a field of science devoted to the study of the lands,
             the features, the inhabitants, and the phenomena of Earth.
            """,
        """
            Literature is a body of written works.
             The name has traditionally been applied to those imaginative
             works of poetry and prose distinguished by the intentions of their authors.
            """,
        """
            Art is a diverse range of human activities in creating visual,
             auditory or performing artifacts (artworks),
             expressing the author's imaginative or technical skill,
             intended to be appreciated for their beauty or emotional power.
            """,
        """
            Music is an art form and cultural activity whose medium is sound organized in time.
             General definitions of music include common elements such as pitch
             (which governs melody and harmony),
             rhythm (and its associated concepts tempo, meter, and articulation),
             dynamics, and the sonic qualities of timbre and texture.
            """
    };

    private void clearTables() {
        String query =
            """
                DROP TABLE IF EXISTS students_courses;
                DROP TABLE IF EXISTS students;
                DROP TABLE IF EXISTS groups;
                DROP TABLE IF EXISTS courses;
                """;
        daoFactory.doPost(query);
    }

    private void createTables() {
        String query =
            """
                CREATE TABLE students
                (
                    student_id SERIAL,
                    group_id   INT          NOT NULL,
                    first_name VARCHAR(255) NOT NULL,
                    last_name  VARCHAR(255) NOT NULL,
                    PRIMARY KEY (student_id)
                );
                            
                CREATE TABLE groups
                (
                    group_id   SERIAL,
                    group_name VARCHAR(255) NOT NULL,
                    PRIMARY KEY (group_id)
                );
                                
                CREATE TABLE courses
                (
                    course_id          SERIAL,
                    course_name        VARCHAR(255) NOT NULL,
                    course_description VARCHAR(255) NOT NULL,
                    PRIMARY KEY (course_id)
                );
                                
                CREATE TABLE students_courses
                (
                    student_id INT NOT NULL,
                    course_id  INT NOT NULL,
                    PRIMARY KEY (student_id, course_id),
                    FOREIGN KEY (student_id) REFERENCES students (student_id),
                    FOREIGN KEY (course_id) REFERENCES courses (course_id)
                );
                """;
        daoFactory.doPost(query);
    }
}
