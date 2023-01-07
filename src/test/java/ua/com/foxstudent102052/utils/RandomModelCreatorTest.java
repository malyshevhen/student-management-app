package ua.com.foxstudent102052.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomModelCreatorTest {
    public static final String COURSES_CSV = "csv/courses.csv";
    public static final String GROUPS_CSV = "csv/groups.csv";
    public static final String STUDENT_NAMES_CSV = "csv/student_names.csv";
    public static final String STUDENT_SURNAMES_CSV = "csv/student_surnames.csv";
    static List<String[]> coursesNamesAndDescriptions;
    static List<String> groupNames;
    static List<String> studentNames;
    static List<String> studentSurnames;

    @BeforeAll
    static void init() {
        FileUtils fileUtils = new FileUtils();

        coursesNamesAndDescriptions = fileUtils.readCsvFileFromResources(COURSES_CSV);
        groupNames = fileUtils.readCsvFileFromResources(GROUPS_CSV).stream()
            .map(s -> s[0])
            .toList();
        studentNames = fileUtils.readCsvFileFromResources(STUDENT_NAMES_CSV).stream()
            .map(s -> s[0])
            .toList();
        studentSurnames = fileUtils.readCsvFileFromResources(STUDENT_SURNAMES_CSV).stream()
            .map(s -> s[0])
            .toList();
    }

    @Test
    void Method_getGroups_shouldReturnListOfGroups() {
        // given
        var groups = RandomModelCreator.getGroups(groupNames);

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
        var courses = RandomModelCreator.getCourses(coursesNamesAndDescriptions);

        // when
        var actual = courses.stream()
            .filter(course -> course.getName() == null || course.getDescription() == null)
            .toList();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void Method_getStudents_shouldReturnListOfStudents() {
        // given
        var students = RandomModelCreator.getStudents(studentNames, studentSurnames, 10, 100);

        // when
        var actual = students.stream()
            .filter(student -> student.getFirstName() == null || student.getLastName() == null || student.getGroup() == null)
            .toList();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void Method_getStudents_shouldReturnListOfStudents_withGroupIdsInGivenRange() {
        // given
        var students = RandomModelCreator.getStudents(studentNames, studentSurnames, 10, 100);

        // when
        var actual = students.stream()
            .mapToInt(student -> student.getGroup().getId())
            .anyMatch(id -> id < 0 || id > 9);

        // then
        assertFalse(actual);
    }

    @Test
    void Method_getStudentsCoursesRelations_shouldReturnMapOfStudentCoursesRelations() {
        // given
        int studentsCount = 100;
        int coursesCount = 10;

        // when
        var actual = RandomModelCreator.getStudentsCoursesRelations(studentsCount, coursesCount);

        // then
        assertEquals(actual.size(), studentsCount);
    }

    @Test
    void Method_getStudentsCoursesRelations_shouldReturnMapOfStudentCoursesRelations_withMaxThreeValuesPerKey() {
        // given
        int studentsCount = 100;
        int coursesCount = 10;

        // when
        var actual = RandomModelCreator.getStudentsCoursesRelations(studentsCount, coursesCount);
        boolean anyMatch = actual.values().stream()
            .mapToInt(Set::size)
            .anyMatch(size -> size <= 3 || size >= 1);

        // then
        assertTrue(anyMatch);
    }
}
