package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.*;
import ua.com.foxstudent102052.service.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Slf4j
public class TestDataController {
    DAOFactory daoFactory = DAOFactoryImpl.getInstance();
    StudentRepository studentRepository = new StudentRepositoryImpl(daoFactory);
    CourseRepository courseRepository = new CourseRepositoryImpl(daoFactory);
    GroupRepository groupRepository = new GroupRepositoryImpl(daoFactory);
    StudentService studentService = new StudentServiceImpl(studentRepository);
    CourseService courseService = new CourseServiceImpl(courseRepository);
    GroupService groupService = new GroupServiceImpl(groupRepository);


    private static final String[] firstNames = {
        "John", "Jack", "Bob", "Mike", "Tom",
        "Alex", "Nick", "Sam", "Bill", "George",
        "Kate", "Liza", "Ann", "Jane", "Marry",
        "Linda", "Sara", "Kate", "Liza", "Ann"
    };
    private static final String[] lastNames = {
        "Smith", "Johnson", "Williams", "Brown", "Jones",
        "Miller", "Davis", "Garcia", "Rodriguez", "Wilson",
        "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez",
        "Moore", "Martin", "Jackson", "Thompson", "White"
    };
    private static final String[] groupNames = {
        "GR-001", "GR-002", "GR-003", "GR-004", "GR-005",
        "GR-006", "GR-007", "GR-008", "GR-009", "GR-010"
    };
    private static final String[] courseNames = {
        "Mathematics", "Biology", "Chemistry", "Physics", "English",
        "History", "Geography", "Literature", "Art", "Music"
    };
    private static final String[] courseDescriptions = {
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
        reCreateTables();
        addCourses();
        addGroups();
        addStudents();
        addStudentsToCourses();
    }

    private void reCreateTables() {
        try {
            daoFactory.executeSqlScript("src/main/resources/Table_creation.sql");
        } catch (IOException e) {
            log.error("Error while creating tables", e);
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
                log.error("Error while adding courses", e);
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
                log.error("Error while adding groups", e);
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
                log.error("Error while adding students", e);
            }
        }
    }

    public void addStudentsToCourses() {
        List<StudentDto> students;
        try {
            students = studentService.getAllStudents();

            for (StudentDto student : students) {
                var courses = new HashSet<Integer>();

                for (int i = 1; i <= 3; i++) {
                    var random = new Random();
                    int courseId = random.nextInt(11) + 1;

                    if (Boolean.FALSE.equals(courses.contains(courseId)) && courseId < 11) {

                        try {
                            studentService.addStudentToCourse(student.getId(), courseId);
                        } catch (ServiceException e) {
                            log.error("Error while adding students to courses", e);
                        }
                        courses.add(courseId);
                    }
                }
            }

        } catch (ServiceException e) {
            log.error("Error while getting all students", e);
        }
    }
}
