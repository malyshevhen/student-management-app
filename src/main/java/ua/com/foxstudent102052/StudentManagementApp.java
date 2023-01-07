package ua.com.foxstudent102052;

import ua.com.foxstudent102052.controller.TestDataInitializer;
import ua.com.foxstudent102052.client.ConsoleUI;

public class StudentManagementApp {
    private static final TestDataInitializer testDataInitializer = new TestDataInitializer();
    private static final ConsoleUI consoleUI = new ConsoleUI();

    public static void main(String[] args) {
        testDataInitializer.initTestDada();
        consoleUI.callMainMenu();
    }
}
