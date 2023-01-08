package ua.com.foxstudent102052;

import ua.com.foxstudent102052.client.ConsoleUI;
import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.controller.TestDataInitializer;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.impl.CourseDaoImpl;
import ua.com.foxstudent102052.dao.impl.GroupDaoImpl;
import ua.com.foxstudent102052.dao.impl.StudentDaoImpl;
import ua.com.foxstudent102052.service.impl.CourseServiceImpl;
import ua.com.foxstudent102052.service.impl.GroupServiceImpl;
import ua.com.foxstudent102052.service.impl.StudentServiceImpl;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

public class StudentManagementApp {
    private static final GroupService groupService;
    private static final StudentService studentService;
    private static final CourseService courseService;
    private static final GroupController groupController;
    private static final CourseController courseController;
    private static final StudentController studentController;
    private static final TestDataInitializer testDataInitializer;
    private static final ConsoleUI consoleUI;

    static {
        var customDataSource = PooledDataSource.getInstance();

        var courseRepository = new CourseDaoImpl(customDataSource);
        var studentRepository = new StudentDaoImpl(customDataSource);
        var groupRepository = new GroupDaoImpl(customDataSource);

        courseService = new CourseServiceImpl(courseRepository);
        studentService = new StudentServiceImpl(studentRepository);
        groupService = new GroupServiceImpl(groupRepository);

        groupController = new GroupController(groupService);
        courseController = new CourseController(courseService, studentService);
        studentController = new StudentController(studentService, groupService, courseService);

        testDataInitializer = new TestDataInitializer(studentService, courseService, groupService);
        consoleUI = new ConsoleUI(groupController, courseController, studentController);
    }

    public static void main(String[] args) {
        testDataInitializer.initTestDada();
        consoleUI.callMainMenu();
    }
}
