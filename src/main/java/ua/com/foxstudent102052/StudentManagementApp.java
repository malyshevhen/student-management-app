package ua.com.foxstudent102052;

import ua.com.foxstudent102052.controller.TestDataController;
import ua.com.foxstudent102052.facade.Facade;

public class StudentManagementApp {
    private static final TestDataController testDataController = new TestDataController();

    public static void main(String[] args) {
        testDataController.updateTestDada();
        Facade.callUpMenu();
    }
}
