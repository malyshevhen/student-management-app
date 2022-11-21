package ua.com.foxstudent102052.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.CourseRepository;
import ua.com.foxstudent102052.repository.CourseRepositoryImpl;
import ua.com.foxstudent102052.repository.DAOException;
import ua.com.foxstudent102052.repository.DAOFactory;
import ua.com.foxstudent102052.repository.DAOFactoryImpl;
import ua.com.foxstudent102052.repository.GroupRepository;
import ua.com.foxstudent102052.repository.GroupRepositoryImpl;
import ua.com.foxstudent102052.repository.StudentRepository;
import ua.com.foxstudent102052.repository.StudentRepositoryImpl;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.CourseServiceImpl;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.GroupServiceImpl;
import ua.com.foxstudent102052.service.ServiceException;
import ua.com.foxstudent102052.service.StudentService;
import ua.com.foxstudent102052.service.StudentServiceImpl;

public class TestDataController {
    DAOFactory daoFactory = DAOFactoryImpl.getInstance();
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
                     auditory or performing artworks,
                     expressing the authors imaginative or technical skill,
                     intended to be appreciated for their beauty or emotional power.""",
            """
                    Music is an art form and cultural activity whose medium is sound organized in time.
                     General definitions of music include common elements such as pitch,
                     rhythm, dynamics, and the sonic qualities of timbre and texture."""
    };

    public void updateTestDada() {
        dropTables();
        createTables();
        addCourses();
        addGroups();
        addStudents();
        addStudentsToCourses();
    }

    private void dropTables() {
        String query = """
                DROP TABLE IF EXISTS students_courses;
                DROP TABLE IF EXISTS students;
                DROP TABLE IF EXISTS groups;
                DROP TABLE IF EXISTS courses;
                """;

        try {
            daoFactory.doPost(query);
        } catch (DAOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void createTables() {
        String query = """
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
                );""";

        try {
            daoFactory.doPost(query);
        } catch (DAOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void addCourses() {

        for (int i = 0; i < 10; i++) {

            try {
                var courseDto = CourseDto.builder()
                        .name(courseNames[i])
                        .description(courseDescriptions[i])
                        .build();
                courseService.addCourse(courseDto);

            } catch (ServiceException e) {
                print(e.getMessage());
            }
        }
    }

    private void addGroups() {

        for (String groupName : groupNames) {

            try {

                var groupDto = GroupDto.builder()
                        .name(groupName)
                        .build();
                groupService.addGroup(groupDto);

            } catch (ServiceException e) {
                print(e.getMessage());
            }
        }
    }

    public void addStudents() {

        for (int i = 0; i < 200; i++) {

            try {

                var random = new Random();
                var studentDto = StudentDto.builder()
                        .groupId(random.nextInt(11))
                        .firstName(firstNames[random.nextInt(19)])
                        .lastName(lastNames[random.nextInt(19)])
                        .build();
                studentService.addStudent(studentDto);

            } catch (ServiceException e) {
                print(e.getMessage());
            }
        }
    }

    public void addStudentsToCourses() {
        List<StudentDto> students = List.of();
        try {
            students = studentService.getAllStudents();

            students
                    .forEach(
                            student -> {
                                var courses = new HashSet<Integer>();

                                for (int i = 1; i <= 3; i++) {
                                    var random = new Random();
                                    int courseId = random.nextInt(11) + 1;

                                    if (Boolean.FALSE.equals(courses.contains(courseId)) && courseId < 11) {

                                        try {
                                            studentService.addStudentToCourse(student.getId(), courseId);
                                        } catch (ServiceException e) {
                                            print(e.getMessage());
                                        }
                                        courses.add(courseId);
                                    }
                                }
                            });
        } catch (ServiceException e) {
            print(e.getMessage());
        }
    }

    private static void print(String e) {
        System.out.println(e);
    }
}
