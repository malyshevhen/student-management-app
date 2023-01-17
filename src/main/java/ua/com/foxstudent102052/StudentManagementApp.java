package ua.com.foxstudent102052;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.client.ConsoleUI;
import ua.com.foxstudent102052.controller.TestDataInitializer;

@SpringBootApplication
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentManagementApp {
    private TestDataInitializer testDataInitializer;
    private ConsoleUI consoleUI;

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApp.class, args);
    }

    @PostConstruct
    private void runConsoleApp() {
        testDataInitializer.initTestDada();
        consoleUI.callMainMenu();
    }
}
