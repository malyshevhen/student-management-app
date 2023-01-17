package ua.com.foxstudent102052;

import ua.com.foxstudent102052.client.ConsoleUI;
import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.controller.GroupController;
import ua.com.foxstudent102052.controller.StudentController;
import ua.com.foxstudent102052.controller.TestDataInitializer;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.impl.CourseDaoImpl;
import ua.com.foxstudent102052.dao.impl.GroupDaoImpl;
import ua.com.foxstudent102052.dao.impl.PostDAOImpl;
import ua.com.foxstudent102052.dao.impl.StudentDaoImpl;
import ua.com.foxstudent102052.service.QueryPostService;
import ua.com.foxstudent102052.service.impl.CourseServiceImpl;
import ua.com.foxstudent102052.service.impl.GroupServiceImpl;
import ua.com.foxstudent102052.service.impl.StudentServiceImpl;
import ua.com.foxstudent102052.table.TableFactory;
import ua.com.foxstudent102052.utils.ConsoleUtils;
import ua.com.foxstudent102052.utils.FileUtils;

public class StudentManagementApp {
    private static final TestDataInitializer testDataInitializer;
    private static final ConsoleUI consoleUI;

    static {
        var customDataSource = PooledDataSource.getInstance();
        var fileUtils = new FileUtils();
        var consoleUtils = new ConsoleUtils();
        var tableFactory = new TableFactory();

        var courseRepository = new CourseDaoImpl(customDataSource);
        var studentRepository = new StudentDaoImpl(customDataSource);
        var groupRepository = new GroupDaoImpl(customDataSource);
        var postDAO = new PostDAOImpl(customDataSource);

        var courseService = new CourseServiceImpl(courseRepository);
        var studentService = new StudentServiceImpl(studentRepository);
        var groupService = new GroupServiceImpl(groupRepository);
        var queryPostService = new QueryPostService(postDAO);

        var groupController = new GroupController(groupService, studentService);
        var courseController = new CourseController(courseService, studentService);
        var studentController = new StudentController(studentService, groupService, courseService);

        testDataInitializer = new TestDataInitializer(studentService, courseService, groupService, queryPostService,
                fileUtils);
        consoleUI = new ConsoleUI(groupController, courseController, studentController, consoleUtils, tableFactory);
    }

    public static void main(String[] args) {
        testDataInitializer.initTestDada();
        consoleUI.callMainMenu();
    }
}
