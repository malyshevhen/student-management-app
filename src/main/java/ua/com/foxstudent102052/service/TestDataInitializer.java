package ua.com.foxstudent102052.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestDataInitializer {
    public static final String SCRIPTS_DDL_TABLE_CREATION = "scripts/ddl/clear_tables.sql";
    public static final String COURSES_CSV = "csv/courses.csv";
    public static final String GROUPS_CSV = "csv/groups.csv";
    public static final String STUDENT_NAMES_CSV = "csv/student_names.csv";
    public static final String STUDENT_SURNAMES_CSV = "csv/student_surnames.csv";
    public static final int STUDENTS_COUNT = 200;
    public static final int MAX_COUNT_OF_COURSES = 3;

    private final StudentService studentService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final QueryPostService queryPostService;
    private final RandomModelCreator randomModelCreator;
    private final FileUtils fileUtils;

    public void initTestDada() {
        var coursesNamesAndDescriptions = fileUtils.readCsvFileFromResources(COURSES_CSV);
        var groupNames = fileUtils.readCsvFileFromResources(GROUPS_CSV).stream()
                .map(s -> s[0])
                .toList();
        var studentNames = fileUtils.readCsvFileFromResources(STUDENT_NAMES_CSV).stream()
                .map(s -> s[0])
                .toList();
        var studentSurnames = fileUtils.readCsvFileFromResources(STUDENT_SURNAMES_CSV).stream()
                .map(s -> s[0])
                .toList();

        runDdlScript(queryPostService);

        var courses = randomModelCreator.getCourses(coursesNamesAndDescriptions);
        addCourses(courses);
        var groups = randomModelCreator.getGroups(groupNames);
        addGroups(groups);

        var groupsFromDB = groupService.getAll();
        var students = randomModelCreator.getStudents(studentNames, studentSurnames, groupsFromDB, STUDENTS_COUNT);
        addStudents(students);

        var coursesFromBD = courseService.getAll();
        var studentsFromDB = studentService.getAll();
        addStudentsToCourses(studentsFromDB, coursesFromBD);
    }

    private void runDdlScript(QueryPostService queryPostService) {
        try {
            var query = fileUtils.readFileFromResourcesAsString(SCRIPTS_DDL_TABLE_CREATION);

            queryPostService.executeQuery(query);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    private void addCourses(List<CourseDto> courses) {
        for (var course : courses) {
            try {
                courseService.addCourse(course);
            } catch (DataAccessException | ElementAlreadyExistException e) {
                log.error("Error while adding courses", e);
            }
        }
    }

    private void addGroups(List<GroupDto> groups) {
        for (var group : groups) {
            try {
                groupService.addGroup(group);
            } catch (DataAccessException | ElementAlreadyExistException e) {
                log.error("Error while adding groups", e);
            }
        }
    }

    private void addStudents(List<StudentDto> students) {
        for (var student : students) {
            try {
                studentService.addStudent(student);
            } catch (DataAccessException e) {
                log.error("Error while adding students", e);
            }
        }
    }

    private void addStudentsToCourses(List<StudentDto> studentDtoList, List<CourseDto> courseDtoList) {
        var relationMap = randomModelCreator.getStudentsCoursesRelations(studentDtoList, courseDtoList,
                MAX_COUNT_OF_COURSES);

        for (var relation : relationMap.entrySet()) {
            int studentId = relation.getKey();
            var courseIdSet = relation.getValue();

            for (var courseId : courseIdSet) {
                try {
                    studentService.addStudentToCourse(studentId, courseId);
                } catch (NoSuchElementException | DataAccessException e) {
                    log.error("Error while adding students to courses", e);
                }
            }
        }
    }
}
