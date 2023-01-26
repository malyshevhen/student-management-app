package ua.com.foxstudent102052.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.testinit.TestDataRepository;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@Slf4j(topic = "FILE")
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestDataInitializer {
    public static final String COURSES_CSV = "csv/courses.csv";
    public static final String GROUPS_CSV = "csv/groups.csv";
    public static final String STUDENT_NAMES_CSV = "csv/student_names.csv";
    public static final String STUDENT_SURNAMES_CSV = "csv/student_surnames.csv";
    public static final int STUDENTS_COUNT = 200;
    public static final int MAX_COUNT_OF_COURSES = 3;

    private final TestDataRepository testDataRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final RandomModelCreator randomModelCreator;
    private final FileUtils fileUtils;

    public void initTestDada() {
        var coursesNamesAndDescriptions = fileUtils.readCsvFileFromResources(COURSES_CSV);
        var groupNames = fileUtils.readCsvFileFromResources(GROUPS_CSV).stream().map(s -> s[0]).toList();
        var studentNames = fileUtils.readCsvFileFromResources(STUDENT_NAMES_CSV).stream().map(s -> s[0]).toList();
        var studentSurnames = fileUtils.readCsvFileFromResources(STUDENT_SURNAMES_CSV).stream().map(s -> s[0]).toList();

        var students = randomModelCreator.getStudents(studentNames, studentSurnames, STUDENTS_COUNT);
        var courses = randomModelCreator.getCourses(coursesNamesAndDescriptions);
        var groups = randomModelCreator.getGroups(groupNames);

        testDataRepository.postTestRecords(students, courses, groups);

        log.debug("TEST DATA INITIALIZATION FINISHED!");

        var coursesIds = courseService.getAll().stream().mapToInt(CourseDto::getCourseId).toArray();
        var studentIds = studentService.getAll().stream().mapToInt(StudentDto::getStudentId).toArray();

        addStudentsToCourses(studentIds, coursesIds);

        log.debug("COURSE-STUDENT RELATIONS POST TO DB!");
    }

    private void addStudentsToCourses(int[] studentIds, int[] coursesIds) {
        var relationMap = randomModelCreator
                .getStudentsCoursesRelations(studentIds, coursesIds, MAX_COUNT_OF_COURSES);

        for (var relation : relationMap.entrySet()) {
            int studentId = relation.getKey();
            var courseIdSet = relation.getValue();

            for (var courseId : courseIdSet) {
                try {
                    studentService.addStudentToCourse(studentId, courseId);
                } catch (NoSuchElementException | DataAccessException e) {
                    log.debug("Error while adding students to courses", e);
                }
            }
        }
    }
}
