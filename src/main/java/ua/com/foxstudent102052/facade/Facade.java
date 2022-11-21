package ua.com.foxstudent102052.facade;

import static ua.com.foxstudent102052.facade.InputUtils.takeInputIntFromUser;
import static ua.com.foxstudent102052.facade.InputUtils.takeInputStringFromUser;
import static ua.com.foxstudent102052.facade.TableBuilder.createCourseTable;
import static ua.com.foxstudent102052.facade.TableBuilder.createGroupStudentsTable;
import static ua.com.foxstudent102052.facade.TableBuilder.createGroupTable;
import static ua.com.foxstudent102052.facade.TableBuilder.createStudentTable;

import java.util.NoSuchElementException;

import ua.com.foxstudent102052.controller.ControllerException;
import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.CourseRepository;
import ua.com.foxstudent102052.repository.CourseRepositoryImpl;
import ua.com.foxstudent102052.repository.DAOFactory;
import ua.com.foxstudent102052.repository.DAOFactoryImpl;
import ua.com.foxstudent102052.repository.GroupRepository;
import ua.com.foxstudent102052.repository.GroupRepositoryImpl;
import ua.com.foxstudent102052.repository.StudentRepository;
import ua.com.foxstudent102052.repository.StudentRepositoryImpl;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.CourseServiceImpl;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.GroupServiceImpl;
import ua.com.foxstudent102052.service.StudentService;
import ua.com.foxstudent102052.service.StudentServiceImpl;

public class Facade {

    public static final String ENTER_GROUP_ID = "Enter group id: ";

    private Facade() {
        throw new IllegalStateException("Utility class");
    }

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
    private static final String WRONG_INPUT = "Wrong input.";

    private static final DAOFactory daoFactory = DAOFactoryImpl.getInstance();
    private static final GroupRepository groupRepository = new GroupRepositoryImpl(daoFactory);
    private static final CourseRepository courseRepository = new CourseRepositoryImpl(daoFactory);
    private static final StudentRepository studentRepository = new StudentRepositoryImpl(daoFactory);
    private static final GroupService groupService = new GroupServiceImpl(groupRepository);
    private static final CourseService courseService = new CourseServiceImpl(courseRepository);
    private static final StudentService studentService = new StudentServiceImpl(studentRepository);
    private static final GroupController groupController = new GroupController(groupService);
    private static final CourseController courseController = new CourseController(courseService, studentService);
    private static final StudentController studentController = new StudentController(
            studentService, groupService, courseService);

    private static int option = Integer.MAX_VALUE;

    public static void callUpMenu() {
        while (option != 0) {
            option = takeInputIntFromUser(STUDENT_MENU);

            try {
                if (option == 1) {
                    var firstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                    var lastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                    print("Choose students group from list:");

                    try {
                        var allGroups = groupController.getAllGroups();
                        print(createGroupTable(allGroups));
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }

                    int groupId = takeInputIntFromUser(ENTER_OPTION_NUMBER);
                    var studentDto = StudentDto.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .groupId(groupId)
                            .build();

                    try {
                        studentController.addStudent(studentDto);
                        print("Student added successfully");
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }

                } else if (option == 2) {
                    int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);

                    try {
                        studentController.removeStudent(studentId);
                        print("Student removed successfully");
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }

                } else if (option == 3) {
                    int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);

                    try {
                        var allCourses = courseController.getAllCourses();
                        print(createCourseTable(allCourses));
                        int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                        studentController.addStudentToCourse(studentId, courseId);
                        print("Student added to course successfully");
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }

                } else if (option == 4) {
                    int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                    print("Choose course to remove from:");

                    try {
                        var allCourses = courseController.getAllCourses();
                        print(createCourseTable(allCourses));
                        int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                        studentController.removeStudentFromCourse(studentId, courseId);
                        print("Student removed from course successfully");
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }
                } else if (option == 5) {
                    int numberOfStudents = takeInputIntFromUser("Enter minimum number of students: ");

                    try {
                        var groupTable = createGroupStudentsTable(groupController.getGroupsSmallerThen(numberOfStudents));
                        print(groupTable);
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }

                } else if (option == 6) {

                    try {
                        var studentName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                    print(createCourseTable(courseController.getAllCourses()));
                    var courseId = takeInputIntFromUser(ENTER_GROUP_ID);
                    var studentsByCourseNameAndGroupId = studentController
                            .getStudentsByNameAndCourse(studentName, courseId);
                    var studentTable = createStudentTable(studentsByCourseNameAndGroupId);
                    print(studentTable);

                } catch (ControllerException e) {
                    print(e.getMessage());
                }
                } else if (option == 7) {

                    try {
                       var allStudents = studentController.getAllStudents();
                       print(createStudentTable(allStudents));
                    } catch (ControllerException e) {
                        print(e.getMessage());
                    }

                } else if (option == 0) {
                    break;
                } else {
                    print(WRONG_INPUT);
                }

            } catch (NoSuchElementException | IllegalArgumentException e) {
                print(e.getMessage());
            }
        }

        print("Thank you for using Student Management Application!");
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
