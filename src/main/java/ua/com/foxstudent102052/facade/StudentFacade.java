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

    private static final DAOFactory daoFactory = new DAOFactoryImpl();
    private static final GroupRepository groupRepository = new GroupRepositoryImpl(daoFactory);
    private static final GroupService groupService = new GroupServiceImpl(groupRepository);
    private static final GroupController groupController = new GroupController(groupService);
    private static final CourseRepository courseRepository = new CourseRepositoryImpl(daoFactory);
    private static final CourseService courseService = new CourseServiceImpl(courseRepository);
    private static final CourseController courseController = new CourseController(courseService);
    private static final StudentRepository studentRepository = new StudentRepositoryImpl(daoFactory);
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
                var firstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                var lastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                print("Choose students group from list:");
                print(createGroupStudentsTable(groupController.getAllGroups()));
                int groupId = takeInputIntFromUser(ENTER_OPTION_NUMBER);

                var studentDto = StudentDto.builder()
                    .fistName(firstName)
                    .lastName(lastName)
                    .groupId(groupId)
                    .build();
                studentController.addStudent(studentDto);

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
                print(WRONG_INPUT);
            }
        }
    }

    static void callStudentSearchMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(FIND_STUDENT_MENU);

            if (option == 1) {
                var allStudents = studentController.getAllStudents();
                print(createStudentTable(allStudents));

            } else if (option == 2) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                var studentById = studentController.getStudentById(studentId);
                print(createStudentTable(studentById));

            } else if (option == 3) {
                var studentFirstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                var studentsByName = studentController.getStudentsByName(studentFirstName);
                print(createStudentTable(studentsByName));

            } else if (option == 4) {
                var studentLastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                var studentsByLastName = studentController.getStudentsByLastName(studentLastName);
                print(createStudentTable(studentsByLastName));

            } else if (option == 5) {
                int studentGroupId = takeInputIntFromUser(ENTER_GROUP_ID);
                var studentsByGroupId = studentController.getStudentsByGroupId(studentGroupId);
                print(createStudentTable(studentsByGroupId));

            } else if (option == 6) {
                int studentCourseId = takeInputIntFromUser(ENTER_COURSE_ID);
                var studentsByCourseId = studentController.getStudentsByCourseId(studentCourseId);
                print(createStudentTable(studentsByCourseId));

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                callStudentManagementMenu();

            } else {
                print(WRONG_INPUT);
            }
        }
    }

    static void callStudentUpdateMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(UPDATE_STUDENT_MENU);

            if (option == 1) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                var studentFirstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                studentController.updateStudentFirstName(studentId, studentFirstName);

            } else if (option == 2) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                var studentLastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                studentController.updateStudentsLastName(studentId, studentLastName);

            } else if (option == 3) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                var allGroups = groupController.getAllGroups();
                print(createGroupStudentsTable(allGroups));
                int groupId = takeInputIntFromUser(ENTER_GROUP_ID);
                studentController.addStudentToGroup(studentId, groupId);

            } else if (option == 4) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                print("Choose course to remove:");
                var allCourses = courseController.getAllCourses();
                print(createCourseTable(allCourses));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                studentController.removeStudentFromCourse(studentId, courseId);

            } else if (option == 5) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                var allCourses = courseController.getAllCourses();
                print(createCourseTable(allCourses));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                studentController.addStudentToCourse(studentId, courseId);

            } else if (option == 6) {
                var id = takeInputIntFromUser(ENTER_STUDENT_ID);
                var groupId = takeInputIntFromUser(ENTER_GROUP_ID);
                var fistName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                var lastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                var studentDto = StudentDto.builder()
                    .id(id)
                    .groupId(groupId)
                    .fistName(fistName)
                    .lastName(lastName)
                    .build();
                studentController.updateStudent(studentDto);

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                callStudentManagementMenu();

            } else {
                print(WRONG_INPUT);
            }
        }
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
