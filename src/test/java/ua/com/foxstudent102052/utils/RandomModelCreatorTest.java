package ua.com.foxstudent102052.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

class RandomModelCreatorTest {
    public static final String COURSES_CSV = "csv/courses.csv";
    public static final String GROUPS_CSV = "csv/groups.csv";
    public static final String STUDENT_NAMES_CSV = "csv/student_names.csv";
    public static final String STUDENT_SURNAMES_CSV = "csv/student_surnames.csv";

    private final RandomModelCreator randomModelCreator = new RandomModelCreator();

    static List<String[]> coursesNamesAndDescriptions;
    static List<String> groupNames;
    static List<String> studentNames;
    static List<String> studentSurnames;
    static List<Group> groups;
    static List<Student> students;
    static List<Course> courses;

    @BeforeAll
    static void init() {
        FileUtils fileUtils = new FileUtils();

        coursesNamesAndDescriptions = fileUtils.readCsvFileFromResources(COURSES_CSV);
        groupNames = fileUtils.readCsvFileFromResources(GROUPS_CSV).stream().map(s -> s[0]).toList();
        studentNames = fileUtils.readCsvFileFromResources(STUDENT_NAMES_CSV).stream().map(s -> s[0]).toList();
        studentSurnames = fileUtils.readCsvFileFromResources(STUDENT_SURNAMES_CSV).stream().map(s -> s[0]).toList();
        groups = List.of(
                Group.builder()
                        .id(1L)
                        .name("Group1")
                        .build(),
                Group.builder()
                        .id(2L)
                        .name("Group2")
                        .build(),
                Group.builder()
                        .id(3L)
                        .name("Group3")
                        .build(),
                Group.builder()
                        .id(4L)
                        .name("Group4")
                        .build());
        students = List.of(
                Student.builder()
                        .id(0L)
                        .build(),
                Student.builder()
                        .id(1L)
                        .build(),
                Student.builder()
                        .id(2L)
                        .build(),
                Student.builder()
                        .id(3L)
                        .build(),
                Student.builder()
                        .id(4L)
                        .build(),
                Student.builder()
                        .id(5L)
                        .build());
        courses = List.of(
                Course.builder()
                        .id(1L)
                        .build(),
                Course.builder()
                        .id(2L)
                        .build(),
                Course.builder()
                        .id(3L)
                        .build());
    }

    @Test
    void Method_getGroups_shouldReturnListOfGroups() {
        // given
        var groups = randomModelCreator.getGroups(groupNames);

        // when
        var actual = groups.stream()
                .filter(group -> group.getName() == null)
                .toList();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void Method_getCourses_shouldReturnListOfCourses() {
        // given
        var courses = randomModelCreator.getCourses(coursesNamesAndDescriptions);

        // when
        var actual = courses.stream()
                .filter(course -> course.getName() == null ||
                        course.getDescription() == null)
                .toList();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void Method_getStudents_shouldReturnListOfStudents() {
        // given
        var students = randomModelCreator.getStudents(studentNames, studentSurnames, 100);

        // when
        var actual = students.stream()
                .filter(student -> student.getFirstName() == null ||
                        student.getLastName() == null)
                .toList();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void Method_getStudentsCoursesRelations_shouldReturnMapOfStudentCoursesRelations() {
        // given
        int coursesCount = 2;
        var studentIds = students.stream().mapToLong(Student::getId).toArray();
        var courseIds = courses.stream().mapToLong(Course::getId).toArray();

        // when
        var actual = randomModelCreator.getStudentsCoursesRelations(studentIds, courseIds, coursesCount);

        // then
        assertEquals(actual.size(), students.size());
    }

    @Test
    void Method_getStudentsCoursesRelations_shouldReturnMapOfStudentCoursesRelations_withMaxThreeValuesPerKey() {
        // given
        int coursesCount = 3;
        var studentIds = students.stream().mapToLong(Student::getId).toArray();
        var courseIds = courses.stream().mapToLong(Course::getId).toArray();

        // when
        var actual = randomModelCreator.getStudentsCoursesRelations(studentIds, courseIds, coursesCount);

        boolean anyMatch = actual.values()
                .stream()
                .mapToLong(Set::size)
                .anyMatch(size -> size <= 3 || size >= 1);

        // then
        assertTrue(anyMatch);
    }
}
