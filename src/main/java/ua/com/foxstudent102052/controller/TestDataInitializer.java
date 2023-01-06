package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.impl.CourseDaoImpl;
import ua.com.foxstudent102052.dao.impl.GroupDaoImpl;
import ua.com.foxstudent102052.dao.impl.StudentDaoImpl;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.QueryPostService;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.impl.CourseServiceImpl;
import ua.com.foxstudent102052.service.impl.GroupServiceImpl;
import ua.com.foxstudent102052.service.impl.StudentServiceImpl;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
public class TestDataInitializer {
    public static final int STUDENTS_COUNT = 200;
    public static final int MAX_COUNT_OF_COURSES = 3;

    private static final StudentService studentService;
    private static final CourseService courseService;
    private static final GroupService groupService;
    public static final QueryPostService queryPostService;

    static {
        var customDataSource = PooledDataSource.getInstance();
        var studentRepository = new StudentDaoImpl(customDataSource);
        var courseRepository = new CourseDaoImpl(customDataSource);
        var groupRepository = new GroupDaoImpl(customDataSource);

        studentService = new StudentServiceImpl(studentRepository);
        courseService = new CourseServiceImpl(courseRepository);
        groupService = new GroupServiceImpl(groupRepository);
        queryPostService = new QueryPostService(customDataSource);
    }

    public void initTestDada() {
        runDdlScript();
        addCourses();
        addGroups();
        addStudents();
        addStudentsToCourses();
    }

    private void runDdlScript() {
        try {
            var query = FileUtils.readTextFile("src/main/resources/scripts/ddl/Table_creation.sql");

            queryPostService.executeQuery(query);
        } catch (IOException | DAOException e) {
            log.error(e.getMessage());
        }
    }

    private void addCourses() {
        var courses = RandomData.getCourses();

        for (var course : courses) {
            try {
                courseService.addCourse(course);
            } catch (DAOException | ElementAlreadyExistException e) {
                log.error("Error while adding courses", e);
            }
        }
    }

    private void addGroups() {
        var groups = RandomData.getGroups();
        for (var group : groups) {
            try {
                groupService.addGroup(group);
            } catch (DAOException | ElementAlreadyExistException e) {
                log.error("Error while adding groups", e);
            }
        }
    }

    public void addStudents() {
        for (int i = 0; i < STUDENTS_COUNT; i++) {
            try {
                studentService.addStudent(RandomData.getStudent());
            } catch (DAOException e) {
                log.error("Error while adding students", e);
            }
        }
    }

    public void addStudentsToCourses() {
        var relationMap = RandomData.getStudentCoursesRelations();

        for (var relation : relationMap.entrySet()) {
            int studentId = relation.getKey();
            var courseIdSet = relation.getValue();

            for (var courseId : courseIdSet) {
                try {
                    studentService.addStudentToCourse(studentId, courseId);
                } catch (NoSuchElementException | DAOException e) {
                    log.error("Error while adding students to courses", e);
                }
            }
        }
    }

    static class RandomData {
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

        private static final Random random = new Random();

        private RandomData() {
        }

        static List<GroupDto> getGroups() {
            var groupList = new ArrayList<GroupDto>();

            for (String groupName : groupNames) {
                var group = GroupDto.builder()
                    .name(groupName)
                    .build();
                groupList.add(group);
            }

            return groupList;
        }

        static List<CourseDto> getCourses() {
            int coursesCount = courseNames.length;

            var courses = new ArrayList<CourseDto>();

            for (int i = 0; i < coursesCount; i++) {
                var course = CourseDto.builder()
                    .name(courseNames[i])
                    .description(courseDescriptions[i])
                    .build();
                courses.add(course);
            }

            return courses;
        }

        static StudentDto getStudent() {
            int uniqueStudentsNameCount = 20;

            var group = GroupDto.builder()
                .id(random.nextInt(groupNames.length + 1))
                .build();

            return StudentDto.builder()
                .group(group)
                .firstName(firstNames[random.nextInt(uniqueStudentsNameCount)])
                .lastName(lastNames[random.nextInt(uniqueStudentsNameCount)])
                .build();
        }

        static Map<Integer, Set<Integer>> getStudentCoursesRelations() {
            int coursesCount = courseNames.length;

            var studentCourseMap = new HashMap<Integer, Set<Integer>>();

            for (int i = 1; i <= STUDENTS_COUNT; i++) {
                var courses = new HashSet<Integer>();

                for (int j = 1; j <= MAX_COUNT_OF_COURSES; j++) {
                    int courseId = random.nextInt(coursesCount) + 1;

                    if (Boolean.FALSE.equals(courses.contains(courseId)) && courseId < coursesCount) {
                        courses.add(courseId);
                    }
                }
                studentCourseMap.put(i, courses);
            }

            return studentCourseMap;
        }
    }
}
