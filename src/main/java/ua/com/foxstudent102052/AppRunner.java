package ua.com.foxstudent102052;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.service.TestDataInitializer;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {
    private final TestDataInitializer testDataInitializer;

    @Override
    public void run(ApplicationArguments args) {
        testDataInitializer.initTestDada();
    }
}
