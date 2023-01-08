package ua.com.foxstudent102052.client;

import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.table.TableFactory;
import ua.com.foxstudent102052.table.impl.ExpandedGroupTableBuilder;
import ua.com.foxstudent102052.table.impl.ExpandedStudentTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedCourseTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedGroupTableBuilder;
import ua.com.foxstudent102052.utils.ConsoleUtils;

import java.util.NoSuchElementException;

@AllArgsConstructor
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

    private static final ConsoleUtils consoleUtils = new ConsoleUtils();
    private static final TableFactory tableFactory = new TableFactory();
    private final GroupController groupController;
    private final CourseController courseController;
    private final StudentController studentController;

    public void callMainMenu() {
        while (true) {
            int selectedMenuOption = consoleUtils.getInputInt(STUDENT_MENU);

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
                    consoleUtils.print(WRONG_INPUT_MESSAGE);
                }
            } catch (NoSuchElementException | IllegalArgumentException e) {
                consoleUtils.print(e.getMessage());
            }
        }
        consoleUtils.print("Thank you for using Student Management Application!");
    }

    void callAddStudentMenu() {
        var firstName = consoleUtils.getInputString(ENTER_STUDENT_NAME);
        var lastName = consoleUtils.getInputString(ENTER_STUDENT_SURNAME);

        consoleUtils.print("Choose students group from list:");

        try {
            var allGroups = groupController.getAllGroups();

            var groupTable = tableFactory.buildTable(allGroups, new ReducedGroupTableBuilder());

            consoleUtils.print(groupTable);
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }

        int groupId = consoleUtils.getInputInt(ENTER_OPTION_NUMBER);
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

            consoleUtils.print("Student added successfully");
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    void callRemoveStudentMenu() {
        int studentId = consoleUtils.getInputInt(ENTER_STUDENT_ID);

        try {
            studentController.removeStudent(studentId);

            consoleUtils.print("Student removed successfully");
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    void callAddStudentToCourseMenu() {
        int studentId = consoleUtils.getInputInt(ENTER_STUDENT_ID);

        try {
            var allCourses = courseController.getAllCourses();

            var courseTable = tableFactory.buildTable(allCourses, new ReducedCourseTableBuilder());
            consoleUtils.print(courseTable);

            int courseId = consoleUtils.getInputInt(ENTER_COURSE_ID);
            studentController.addStudentToCourse(studentId, courseId);

            consoleUtils.print("Student added to course successfully");
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    void callRemoveStudentFromCourseMenu() {
        int studentId = consoleUtils.getInputInt(ENTER_STUDENT_ID);

        consoleUtils.print("Choose course to remove from:");

        try {
            var allCourses = courseController.getAllCourses();

            consoleUtils.print(tableFactory.buildTable(allCourses, new ReducedCourseTableBuilder()));
            int courseId = consoleUtils.getInputInt(ENTER_COURSE_ID);
            studentController.removeStudentFromCourse(studentId, courseId);

            consoleUtils.print("Student removed from course successfully");
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    void callFindGroupsMenu() {
        int numberOfStudents = consoleUtils.getInputInt("Enter minimum number of students: ");

        try {
            var groupsSmallerThen = groupController.getGroupsSmallerThen(numberOfStudents);
            var groupTable = tableFactory.buildTable(groupsSmallerThen, new ExpandedGroupTableBuilder());

            consoleUtils.print(groupTable);
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    void callFindStudentMenu() {
        try {
            var studentName = consoleUtils.getInputString(ENTER_STUDENT_NAME);
            var courseTable = tableFactory.buildTable(courseController.getAllCourses(),
                new ReducedCourseTableBuilder());

            consoleUtils.print(courseTable);
            var courseId = consoleUtils.getInputInt(ENTER_GROUP_ID);
            var studentsByCourseNameAndGroupId = studentController
                .getStudents(studentName, courseId);
            var studentTable = tableFactory.buildTable(studentsByCourseNameAndGroupId, new ExpandedStudentTableBuilder());

            consoleUtils.print(studentTable);
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    void callFindAllStudentsMenu() {
        try {
            var allStudents = studentController.getAllStudents();
            var studentTable = tableFactory.buildTable(allStudents, new ExpandedStudentTableBuilder());

            consoleUtils.print(studentTable);
        } catch (DAOException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }
    }
}
