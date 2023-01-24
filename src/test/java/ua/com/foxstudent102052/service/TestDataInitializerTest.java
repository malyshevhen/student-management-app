package ua.com.foxstudent102052.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.dao.testinit.TestDataRepository;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@ExtendWith(MockitoExtension.class)
class TestDataInitializerTest {
    private TestDataRepository testDataRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    @Mock
    private RandomModelCreator randomModelCreator;

    private static TestDataInitializer testDataInitializer;

    @BeforeEach
    void setUp() {
        var fileUtils = new FileUtils();

        testDataInitializer = new TestDataInitializer(testDataRepository, studentService, courseService,
                randomModelCreator, fileUtils);
    }
}
