package ua.com.foxstudent102052.facade;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.controller.CourseController;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.repository.CourseRepository;
import ua.com.foxstudent102052.repository.CourseRepositoryImpl;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.CourseServiceImpl;

import static ua.com.foxstudent102052.facade.InputUtils.takeInputIntFromUser;
import static ua.com.foxstudent102052.facade.InputUtils.takeInputStringFromUser;
import static ua.com.foxstudent102052.facade.TableBuilder.createCourseTable;

@Slf4j
public class CourseFacade {
    private static final String COURSE_MENU =
        """
            -----------------------------------------
            ********** COURSES MANAGEMENT ***********
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Add course;
            2. Remove course;
            3. Update course;
            4. Find course;
            5. Find course by name;
            6. Show all courses;
            0. Back to main menu.
            -----------------------------------------
            Enter option number:""";

    private static final String UPDATE_COURSE_MENU =
        """
            -----------------------------------------
            ********** UPDATE COURSE INFO ***********
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Update course name;
            2. Update course description;
            3. Update full course info;
            0. Back to Courses Management menu.
            -----------------------------------------
            Enter option number:""";

    static final String ENTER_COURSE_ID = "Enter course id: ";
    static final String ENTER_COURSE_NAME = "Enter course name: ";
    static final String ENTER_COURSE_DESCRIPTION = "Enter course description: ";
    static final String WRONG_INPUT = "Wrong input.";
    
    private static CourseRepository courseRepository = CourseRepositoryImpl.getInstance();
    private static final CourseService courseService = new CourseServiceImpl(courseRepository);
    private static final CourseController courseController = new CourseController(courseService);

    private CourseFacade() {
        throw new IllegalStateException("Utility class");
    }

    static int option = Integer.MAX_VALUE;

    static void callCourseManagementMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(COURSE_MENU);

            if (option == 1) {
                String courseName = takeInputStringFromUser(ENTER_COURSE_NAME);
                String courseDescription = takeInputStringFromUser(ENTER_COURSE_DESCRIPTION);
                courseController.addCourse(courseName, courseDescription);

            } else if (option == 2) {
                System.out.println(createCourseTable(courseController
                    .getAllCourses()));
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                courseController.removeCourse(courseId);

            } else if (option == 3) {
                callCourseUpdateMenu();

            } else if (option == 4) {
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                System.out.println(createCourseTable(courseController
                    .getCourseById(courseId)));

            } else if (option == 5) {
                String courseName = takeInputStringFromUser(ENTER_COURSE_NAME);
                System.out.println(createCourseTable(courseController
                    .getCourseByName(courseName)));

            } else if (option == 6) {
                System.out.println(createCourseTable(courseController
                    .getAllCourses()));

            } else if (option == 0) {
                option = Integer.MAX_VALUE;

                MainFacade.callUpMainMenu();

            } else {
                System.out.println(WRONG_INPUT);
            }
        }
    }

    static void callCourseUpdateMenu() {

        while (option != 0) {
            option = takeInputIntFromUser(UPDATE_COURSE_MENU);

            if (option == 1) {
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                String courseName = takeInputStringFromUser(ENTER_COURSE_NAME);
                courseController.updateCourseName(courseId, courseName);

            } else if (option == 2) {
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                String courseDescription = takeInputStringFromUser(ENTER_COURSE_DESCRIPTION);
                courseController.updateCourseDescription(courseId, courseDescription);

            } else if (option == 3) {
                int courseId = takeInputIntFromUser(ENTER_COURSE_ID);
                String courseName = takeInputStringFromUser(ENTER_COURSE_NAME);
                String courseDescription = takeInputStringFromUser(ENTER_COURSE_DESCRIPTION);
                courseController.updateCourse(new CourseDto(courseId, courseName, courseDescription));

            } else if (option == 0) {
                option = Integer.MAX_VALUE;
                callCourseManagementMenu();

            } else {
                System.out.println(WRONG_INPUT);
            }
        }
    }
}
