package ua.com.foxstudent102052;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApp.class, args);
    }
}
