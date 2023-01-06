package ua.com.foxstudent102052.client;

import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.impl.CourseDaoImpl;
import ua.com.foxstudent102052.dao.impl.GroupDaoImpl;
import ua.com.foxstudent102052.dao.impl.StudentDaoImpl;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.impl.CourseServiceImpl;
import ua.com.foxstudent102052.service.impl.GroupServiceImpl;
import ua.com.foxstudent102052.service.impl.StudentServiceImpl;
import ua.com.foxstudent102052.table.TableFactory;
import ua.com.foxstudent102052.table.impl.ExpandedGroupTableBuilder;
import ua.com.foxstudent102052.table.impl.ExpandedStudentTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedCourseTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedGroupTableBuilder;
import ua.com.foxstudent102052.utils.ConsoleUtils;

import java.util.NoSuchElementException;


public class ConsoleUI {

    private static final String ENTER_GROUP_ID = "Enter group id: ";

    private static final String STUDENT_MENU = """
        +----------------------------------------------------------------+
        |********************* STUDENTS MANAGEMENT **********************|
        +----------------------------------------------------------------+
        |Please, choose one of the options below:                        |
        +----------------------------------------------------------------+
        |1. Add student;                                                 |
        |2. Remove student by ID;                                        |
        |3. Add student to course;                                       |
        |4. Remove student from course;                                  |
        |5. Find all groups with less or equal number of students;       |
        |6. Find all students related to the course with the given name; |
        |7. Print all students;                                          |
        |0. Exit.                                                        |
        +----------------------------------------------------------------+
        """;

    private static final String ENTER_STUDENT_ID = "Enter student id: ";
    private static final String ENTER_STUDENT_NAME = "Enter student name: ";
    private static final String ENTER_STUDENT_SURNAME = "Enter student surname: ";
    private static final String ENTER_COURSE_ID = "Enter course id: ";
    private static final String ENTER_OPTION_NUMBER = "Enter option number: ";
    private static final String WRONG_INPUT_MESSAGE = "Wrong input.";


    private static final GroupController groupController;
    private static final CourseController courseController;
    private static final StudentController studentController;
    private static final TableFactory tableFactory;

    static {
        var customDataSource = PooledDataSource.getInstance();
        var courseRepository = new CourseDaoImpl(customDataSource);
        var studentRepository = new StudentDaoImpl(customDataSource);
        var groupRepository = new GroupDaoImpl(customDataSource);
        var courseService = new CourseServiceImpl(courseRepository);
        var studentService = new StudentServiceImpl(studentRepository);
        var groupService = new GroupServiceImpl(groupRepository);

        groupController = new GroupController(groupService);
        courseController = new CourseController(courseService, studentService);
        studentController = new StudentController(studentService, groupService, courseService);
        tableFactory = new TableFactory();
    }

    private ConsoleUI() {
        throw new IllegalStateException("Utility class");
    }

    public static void callMainMenu() {
        while (true) {
            int selectedMenuOption = ConsoleUtils.getInputInt(STUDENT_MENU);

            try {
                if (selectedMenuOption == 1) {
                    callAddStudentMenu();
                } else if (selectedMenuOption == 2) {
                    callRemoveStudentMenu();
                } else if (selectedMenuOption == 3) {
                    callAddStudentToCourseMenu();
                } else if (selectedMenuOption == 4) {
                    callRemoveStudentFromCourseMenu();
                } else if (selectedMenuOption == 5) {
                    callFindGroupsMenu();
                } else if (selectedMenuOption == 6) {
                    callFindStudentMenu();
                } else if (selectedMenuOption == 7) {
                    callFindAllStudentsMenu();
                } else if (selectedMenuOption == 0) {
                    break;
                } else {
                    ConsoleUtils.print(WRONG_INPUT_MESSAGE);
                }
            } catch (NoSuchElementException | IllegalArgumentException e) {
                ConsoleUtils.print(e.getMessage());
            }
        }
        ConsoleUtils.print("Thank you for using Student Management Application!");
    }

    private static void callAddStudentMenu() {
        var firstName = ConsoleUtils.getInputString(ENTER_STUDENT_NAME);
        var lastName = ConsoleUtils.getInputString(ENTER_STUDENT_SURNAME);

        ConsoleUtils.print("Choose students group from list:");

        try {
            var allGroups = groupController.getAllGroups();

            var groupTable = tableFactory.buildTable(allGroups, new ReducedGroupTableBuilder());

            ConsoleUtils.print(groupTable);
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }

        int groupId = ConsoleUtils.getInputInt(ENTER_OPTION_NUMBER);
        var chosenGroup = GroupDto.builder()
            .id(groupId)
            .build();
        var studentDto = StudentDto.builder()
            .firstName(firstName)
            .lastName(lastName)
            .group(chosenGroup)
            .build();

        try {
            studentController.addStudent(studentDto);

            ConsoleUtils.print("Student added successfully");
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }

    private static void callRemoveStudentMenu() {
        int studentId = ConsoleUtils.getInputInt(ENTER_STUDENT_ID);

        try {
            studentController.removeStudent(studentId);

            ConsoleUtils.print("Student removed successfully");
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }

    private static void callAddStudentToCourseMenu() {
        int studentId = ConsoleUtils.getInputInt(ENTER_STUDENT_ID);

        try {
            var allCourses = courseController.getAllCourses();

            var courseTable = tableFactory.buildTable(allCourses, new ReducedCourseTableBuilder());
            ConsoleUtils.print(courseTable);

            int courseId = ConsoleUtils.getInputInt(ENTER_COURSE_ID);
            studentController.addStudentToCourse(studentId, courseId);

            ConsoleUtils.print("Student added to course successfully");
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }

    private static void callRemoveStudentFromCourseMenu() {
        int studentId = ConsoleUtils.getInputInt(ENTER_STUDENT_ID);

        ConsoleUtils.print("Choose course to remove from:");

        try {
            var allCourses = courseController.getAllCourses();

            ConsoleUtils.print(tableFactory.buildTable(allCourses, new ReducedCourseTableBuilder()));
            int courseId = ConsoleUtils.getInputInt(ENTER_COURSE_ID);
            studentController.removeStudentFromCourse(studentId, courseId);

            ConsoleUtils.print("Student removed from course successfully");
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }

    private static void callFindGroupsMenu() {
        int numberOfStudents = ConsoleUtils.getInputInt("Enter minimum number of students: ");

        try {
            var groupsSmallerThen = groupController.getGroupsSmallerThen(numberOfStudents);
            var groupTable = tableFactory.buildTable(groupsSmallerThen, new ExpandedGroupTableBuilder());

            ConsoleUtils.print(groupTable);
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }

    private static void callFindStudentMenu() {
        try {
            var studentName = ConsoleUtils.getInputString(ENTER_STUDENT_NAME);
            var courseTable = tableFactory.buildTable(courseController.getAllCourses(),
                new ReducedCourseTableBuilder());

            ConsoleUtils.print(courseTable);
            var courseId = ConsoleUtils.getInputInt(ENTER_GROUP_ID);
            var studentsByCourseNameAndGroupId = studentController
                .getStudents(studentName, courseId);
            var studentTable = tableFactory.buildTable(studentsByCourseNameAndGroupId, new ExpandedStudentTableBuilder());

            ConsoleUtils.print(studentTable);
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }

    private static void callFindAllStudentsMenu() {
        try {
            var allStudents = studentController.getAllStudents();
            var studentTable = tableFactory.buildTable(allStudents, new ExpandedStudentTableBuilder());

            ConsoleUtils.print(studentTable);
        } catch (DAOException | NoSuchElementException e) {
            ConsoleUtils.print(e.getMessage());
        }
    }
}
