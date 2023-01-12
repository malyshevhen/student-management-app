package ua.com.foxstudent102052;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ua.com.foxstudent102052.client.ConsoleUI;
import ua.com.foxstudent102052.controller.TestDataInitializer;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentManagementApp {
    private TestDataInitializer testDataInitializer;
    private ConsoleUI consoleUI;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ua.com.foxstudent102052");

        var studentManagementApp = context.getBean(StudentManagementApp.class);
        studentManagementApp.testDataInitializer.initTestDada();
        studentManagementApp.consoleUI.callMainMenu();
    }
}
