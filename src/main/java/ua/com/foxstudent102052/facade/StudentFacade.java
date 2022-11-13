package ua.com.foxstudent102052.facade;

import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.*;
import ua.com.foxstudent102052.service.*;

import static ua.com.foxstudent102052.facade.InputUtils.takeInputIntFromUser;
import static ua.com.foxstudent102052.facade.InputUtils.takeInputStringFromUser;
import static ua.com.foxstudent102052.facade.TableBuilder.*;

public class StudentFacade {
    private static final String STUDENT_MENU =
        """
            -----------------------------------------
            ********** STUDENTS MANAGEMENT **********
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Add student;
            2. Remove student;
            3. Update student info;
            4. Find students;
            0. Back to main menu.

            -----------------------------------------
            Enter option number:""";

    private static final String UPDATE_STUDENT_MENU =
        """
            -----------------------------------------
            ********** UPDATE STUDENT INFO **********
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Update student name;
            2. Update student surname;
            3. Update student group;
            4. Remove student from course;
            5. Add student to course;
            6. Update all student info;
            0. Back to Students Management menu.

            -----------------------------------------
            Enter option number:""";

    private static final String FIND_STUDENT_MENU =
        """
            -----------------------------------------
            ************* FIND STUDENTS *************
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Show all students;
            2. Find student by id;
            3. Find student by Name;
            4. Find student by Surname;
            5. Find student by Group;
            6. Find student by Course;
            0. Back to Students Management menu.

            -----------------------------------------
            Enter option number:""";

    private static final String ENTER_STUDENT_ID = "Enter student id: ";
    private static final String ENTER_STUDENT_NAME = "Enter student name: ";
    private static final String ENTER_STUDENT_SURNAME = "Enter student surname: ";
    private static final String ENTER_COURSE_ID = "Enter course id: ";
    private static final String ENTER_GROUP_ID = "Enter group id: ";
    private static final String ENTER_OPTION_NUMBER = "Enter option number: ";
    private static final String WRONG_INPUT = "Wrong input.";

    private static final GroupRepository groupRepository = GroupRepositoryImpl.getInstance();
    private static final GroupService groupService = new GroupServiceImpl(groupRepository);
    private static final GroupController groupController = new GroupController(groupService);
    private static final CourseRepository courseRepository = CourseRepositoryImpl.getInstance();
    private static final CourseService courseService = new CourseServiceImpl(courseRepository);
    private static final CourseController courseController = new CourseController(courseService);
    private static final StudentRepository studentRepository = StudentRepositoryImpl.getInstance();
    private static final StudentService studentService = new StudentServiceImpl(studentRepository);
    private static final StudentController studentController = new StudentController(
        studentService, groupService, courseService);


    private StudentFacade() {
        throw new IllegalStateException("Utility class");
    }

    private static int option = Integer.MAX_VALUE;

    static void callStudentManagementMenu() {
        while (option != 0) {
            option = takeInputIntFromUser(STUDENT_MENU);

            if (option == 1) {
                String firstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                String lastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                System.out.println("Choose students group from list:");
                System.out.println(createGroupStudentsTable(groupController
                    .getAllGroups()));
                int groupId = takeInputIntFromUser(ENTER_OPTION_NUMBER);
                studentController.addStudent(new StudentDto(groupId, firstName, lastName));

            } else if (option == 2) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                studentController.removeStudent(studentId);

            } else if (option == 3) {
                callStudentUpdateMenu();

            } else if (option == 4) {
                callStudentSearchMenu();

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                MainFacade.callUpMainMenu();

            } else {
                System.out.println(WRONG_INPUT);
            }
        }
    }

    static void callStudentSearchMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(FIND_STUDENT_MENU);

            if (option == 1) {
                var allStudents = studentController
                    .getAllStudents();
                System.out.println(createStudentTable(allStudents));

            } else if (option == 2) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                System.out.println(createStudentTable(studentController
                    .getStudentById(studentId)));

            } else if (option == 3) {
                String studentFirstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                System.out.println(createStudentTable(studentController
                    .getStudentsByName(studentFirstName)));

            } else if (option == 4) {
                String studentLastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                System.out.println(createStudentTable(studentController
                    .getStudentsByLastName(studentLastName)));

            } else if (option == 5) {
                int studentGroupId = takeInputIntFromUser(ENTER_GROUP_ID);
                System.out.println(createStudentTable(studentController
                    .getStudentsByGroupId(studentGroupId)));

            } else if (option == 6) {
                int studentCourseId = takeInputIntFromUser(ENTER_COURSE_ID);
                System.out.println(createStudentTable(studentController
                    .getStudentsByCourseId(studentCourseId)));

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                callStudentManagementMenu();

            } else {
                System.out.println(WRONG_INPUT);
            }
        }
    }

    static void callStudentUpdateMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(UPDATE_STUDENT_MENU);

            if (option == 1) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                String studentFirstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                studentController.updateStudentFirstName(studentId, studentFirstName);

            } else if (option == 2) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                String studentLastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                studentController.updateStudentsLastName(studentId, studentLastName);

            } else if (option == 3) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                System.out.println(createGroupStudentsTable(groupController
                    .getAllGroups()));
                int groupId = takeInputIntFromUser(ENTER_GROUP_ID);
                studentController.updateStudentsGroup(studentId, groupId);

            } else if (option == 4) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                System.out.println("Choose course to remove:");
                System.out.println(createCourseTable(courseController
                    .getAllCourses()));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                studentController.removeStudentFromCourse(studentId, courseId);

            } else if (option == 5) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                System.out.println(createCourseTable(courseController
                    .getAllCourses()));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                studentController.addStudentToCourse(studentId, courseId);

            } else if (option == 6) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                int groupId = takeInputIntFromUser(ENTER_GROUP_ID);
                String firstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                String lastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                studentController.updateStudent(new StudentDto(studentId, groupId, firstName, lastName));

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                callStudentManagementMenu();

            } else {
                System.out.println(WRONG_INPUT);
            }
        }
    }
}
