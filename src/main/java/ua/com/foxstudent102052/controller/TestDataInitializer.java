package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.QueryPostService;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestDataInitializer {
    public static final String SCRIPTS_DDL_TABLE_CREATION = "scripts/ddl/Table_creation.sql";
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

        var courses = RandomModelCreator.getCourses(coursesNamesAndDescriptions);
        var groups = RandomModelCreator.getGroups(groupNames);
        var students = RandomModelCreator.getStudents(studentNames, studentSurnames, groups.size(), STUDENTS_COUNT);

        runDdlScript(queryPostService);
        addCourses(courses);
        addGroups(groups);
        addStudents(students);
        addStudentsToCourses();
    }

    private void runDdlScript(QueryPostService queryPostService) {
        try {
            var query = fileUtils.readFileFromResourcesAsString(SCRIPTS_DDL_TABLE_CREATION);

            queryPostService.executeQuery(query);
        } catch (DAOException e) {
            log.error(e.getMessage());
        }
    }

    private void addCourses(List<CourseDto> courses) {
        for (var course : courses) {
            try {
                courseService.addCourse(course);
            } catch (DAOException | ElementAlreadyExistException e) {
                log.error("Error while adding courses", e);
            }
        }
    }

    private void addGroups(List<GroupDto> groups) {
        for (var group : groups) {
            try {
                groupService.addGroup(group);
            } catch (DAOException | ElementAlreadyExistException e) {
                log.error("Error while adding groups", e);
            }
        }
    }

    private void addStudents(List<StudentDto> students) {
        for (var student : students) {
            try {
                studentService.addStudent(student);
            } catch (DAOException e) {
                log.error("Error while adding students", e);
            }
        }
    }

    private void addStudentsToCourses() {
        var relationMap = RandomModelCreator.getStudentsCoursesRelations(STUDENTS_COUNT, MAX_COUNT_OF_COURSES);

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
}
