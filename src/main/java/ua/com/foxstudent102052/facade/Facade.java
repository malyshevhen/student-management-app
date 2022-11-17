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

public class Facade {

    public static final String ENTER_GROUP_ID = "Enter group id: ";

    private Facade(){
        throw new IllegalStateException("Utility class");
    }
    
    private static final String STUDENT_MENU =
        """
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
    
    public static void callUpMenu(){
        while (option != 0){
            option = takeInputIntFromUser(STUDENT_MENU);
            
            if (option == 1) {
                var firstName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                var lastName = takeInputStringFromUser(ENTER_STUDENT_SURNAME);
                print("Choose students group from list:");
                print(createGroupTable(groupController.getAllGroups()));
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
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                var allCourses = courseController.getAllCourses();
                print(createCourseTable(allCourses));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                studentController.addStudentToCourse(studentId, courseId);
            } else if (option == 4) {
                int studentId = takeInputIntFromUser(ENTER_STUDENT_ID);
                print("Choose course to remove from:");
                var allCourses = courseController.getAllCourses();
                print(createCourseTable(allCourses));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                studentController.removeStudentFromCourse(studentId, courseId);
            } else if (option == 5) {
                int numberOfStudents = takeInputIntFromUser("Enter minimum number of students: ");
                var groupTable = createGroupStudentsTable(groupController.getGroupsSmallerThen(numberOfStudents));
                print(groupTable);
            } else if (option == 6) {
                var studentName = takeInputStringFromUser(ENTER_STUDENT_NAME);
                print(createCourseTable(courseController.getAllCourses()));
                var courseId = takeInputIntFromUser(ENTER_GROUP_ID);
                var studentTable = createStudentTable(studentController.getStudentsByCourseNameAndGroupId(studentName, courseId));
                print(studentTable);
            } else if (option == 7) {
                var allStudents = studentController.getAllStudents();
                print(createStudentTable(allStudents));
            } else if (option == 0) {
                break;
            } else {
                print(WRONG_INPUT);
            }
        }
        
        print("Thank you for using Student Management Application!");
    }
    
    private static void print(String message) {
        System.out.println(message);
    }
}
