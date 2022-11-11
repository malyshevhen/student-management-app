package ua.com.foxstudent102052.facade;

public class MainFacade {
    private static final String MAIN_MENU = """
            -----------------------------------------
            *************** MAIN MENU ***************
            -----------------------------------------
            Please, choose one of the options below:
            -----------------------------------------
            1. Students management;
            2. Groups management;
            3. Courses management;
            0. Exit.

            -----------------------------------------
            Enter option number:""";

    private static final String WELCOME_MESSAGE = """
            =========================================
            Welcome To Student Management Application
            =========================================""";

    private static final String WRONG_INPUT = "Wrong input.";

    private MainFacade() {
        throw new IllegalStateException("Utility class");
    }

    private static int option = Integer.MAX_VALUE;

    public static void callUpMainMenu() {

        if (option == Integer.MAX_VALUE) {
            System.out.println(WELCOME_MESSAGE);
        }
        while (true) {
            option = InputUtils.takeInputIntFromUser(MAIN_MENU);
            if (option == 1) {
                StudentFacade.callStudentManagementMenu();
            } else if (option == 2) {
                GroupFacade.callUpGroupManagementMenu();
            } else if (option == 3) {
                CourseFacade.callCourseManagementMenu();
            } else if (option == 0) {
                System.out.println("Thank you for using Student Management Application!");
                System.exit(0);
            } else {
                System.out.println(WRONG_INPUT);
                callUpMainMenu();
            }
        }
    }

}
