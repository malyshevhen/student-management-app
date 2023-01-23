package ua.com.foxstudent102052.client;

import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.table.TableFactory;
import ua.com.foxstudent102052.table.impl.ExpandedGroupTableBuilder;
import ua.com.foxstudent102052.table.impl.ExpandedStudentTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedCourseTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedGroupTableBuilder;
import ua.com.foxstudent102052.utils.ConsoleUtils;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsoleUI {
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
    private static final String ENTER_GROUP_ID = "Enter group id: ";
    private static final String ENTER_COURSE_ID = "Enter course id: ";
    private static final String ENTER_STUDENT_NAME = "Enter student name: ";
    private static final String ENTER_STUDENT_SURNAME = "Enter student surname: ";
    private static final String ENTER_OPTION_NUMBER = "Enter option number: ";
    private static final String WRONG_INPUT_MESSAGE = "Wrong input.";

    private final GroupController groupController;
    private final CourseController courseController;
    private final StudentController studentController;
    private final ConsoleUtils consoleUtils;
    private final TableFactory tableFactory;

    private InputStreamReader reader;

    public void callMainMenu() {
        reader = new InputStreamReader(System.in);
        int selectedMenuOption = consoleUtils.getInputInt(STUDENT_MENU, reader);

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
                consoleUtils.print("Thank you for using Student Management Application!");
            } else {
                consoleUtils.print(WRONG_INPUT_MESSAGE);
                callMainMenu();
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            consoleUtils.print(e.getMessage());
        }
    }

    private void callAddStudentMenu() {
        reader = new InputStreamReader(System.in);
        var firstName = consoleUtils.getInputString(ENTER_STUDENT_NAME, reader);
        var lastName = consoleUtils.getInputString(ENTER_STUDENT_SURNAME, reader);

        consoleUtils.print("Choose students group from list:");

        try {
            var allGroups = groupController.getAllGroups();
            var groupTable = tableFactory.buildTable(allGroups, new ReducedGroupTableBuilder());

            consoleUtils.print(groupTable);
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        }

        int groupId = consoleUtils.getInputInt(ENTER_OPTION_NUMBER, reader);
        var chosenGroup = GroupDto.builder()
                .groupId(groupId)
                .build();
        var studentDto = StudentDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .group(chosenGroup)
                .build();

        try {
            studentController.addStudent(studentDto);

            consoleUtils.print("Student added successfully");
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }
    }

    private void callRemoveStudentMenu() {
        reader = new InputStreamReader(System.in);
        int studentId = consoleUtils.getInputInt(ENTER_STUDENT_ID, reader);

        try {
            studentController.removeStudent(studentId);

            consoleUtils.print("Student removed successfully");
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }
    }

    private void callAddStudentToCourseMenu() {
        reader = new InputStreamReader(System.in);
        int studentId = consoleUtils.getInputInt(ENTER_STUDENT_ID, reader);

        try {
            var allCourses = courseController.getAllCourses();

            var courseTable = tableFactory.buildTable(allCourses, new ReducedCourseTableBuilder());
            consoleUtils.print(courseTable);

            int courseId = consoleUtils.getInputInt(ENTER_COURSE_ID, reader);
            studentController.addStudentToCourse(studentId, courseId);

            consoleUtils.print("Student added to course successfully");
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }

    }

    private void callRemoveStudentFromCourseMenu() {
        reader = new InputStreamReader(System.in);
        int studentId = consoleUtils.getInputInt(ENTER_STUDENT_ID, reader);

        consoleUtils.print("Choose course to remove from:");

        try {
            var allCourses = courseController.getAllCourses();

            consoleUtils.print(tableFactory.buildTable(allCourses, new ReducedCourseTableBuilder()));
            int courseId = consoleUtils.getInputInt(ENTER_COURSE_ID, reader);
            studentController.removeStudentFromCourse(studentId, courseId);

            consoleUtils.print("Student removed from course successfully");
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }
    }

    private void callFindGroupsMenu() {
        reader = new InputStreamReader(System.in);
        int numberOfStudents = consoleUtils.getInputInt("Enter minimum number of students: ", reader);

        try {
            var groupsSmallerThen = groupController.getGroupsSmallerThen(numberOfStudents);
            var groupTable = tableFactory.buildTable(groupsSmallerThen, new ExpandedGroupTableBuilder());

            consoleUtils.print(groupTable);
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }
    }

    private void callFindStudentMenu() {
        reader = new InputStreamReader(System.in);

        try {
            var studentName = consoleUtils.getInputString(ENTER_STUDENT_NAME, reader);
            var allCourses = courseController.getAllCourses();
            var courseTable = tableFactory.buildTable(allCourses,
                    new ReducedCourseTableBuilder());

            consoleUtils.print(courseTable);
            var courseId = consoleUtils.getInputInt(ENTER_GROUP_ID, reader);
            var studentsByCourseNameAndGroupId = studentController
                    .getStudents(studentName, courseId);
            var studentTable = tableFactory.buildTable(studentsByCourseNameAndGroupId,
                    new ExpandedStudentTableBuilder());

            consoleUtils.print(studentTable);
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }
    }

    private void callFindAllStudentsMenu() {
        reader = new InputStreamReader(System.in);

        try {
            var allStudents = studentController.getAllStudents();
            var studentTable = tableFactory.buildTable(allStudents, new ExpandedStudentTableBuilder());

            consoleUtils.print(studentTable);
        } catch (DataAccessException | NoSuchElementException e) {
            consoleUtils.print(e.getMessage());
        } finally {
            callMainMenu();
        }
    }
}
