package ua.com.foxstudent102052.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.testinit.TestDataRepository;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@Service
@Transactional
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
    private final RandomModelCreator randomModelCreator;
    private final FileUtils fileUtils;

    public void initTestDada() {
        try {
            studentService.getAll();
        } catch (NoSuchElementException e) {
            var coursesNamesAndDescriptions = fileUtils.readCsvFileFromResources(COURSES_CSV);
            var groupNames = fileUtils.readCsvFileFromResources(GROUPS_CSV)
                    .stream()
                    .map(s -> s[0])
                    .toList();
            var studentNames = fileUtils.readCsvFileFromResources(STUDENT_NAMES_CSV)
                    .stream()
                    .map(s -> s[0])
                    .toList();
            var studentSurnames = fileUtils.readCsvFileFromResources(STUDENT_SURNAMES_CSV)
                    .stream()
                    .map(s -> s[0])
                    .toList();

            var students = randomModelCreator.getStudents(studentNames, studentSurnames, STUDENTS_COUNT);
            var courses = randomModelCreator.getCourses(coursesNamesAndDescriptions);
            var groups = randomModelCreator.getGroups(groupNames);

            testDataRepository.postTestRecords(students, courses, groups);
        }
    }
}
