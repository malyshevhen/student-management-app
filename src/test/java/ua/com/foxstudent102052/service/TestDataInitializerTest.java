package ua.com.foxstudent102052.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.dao.testinit.TestDataRepository;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@ExtendWith(MockitoExtension.class)
class TestDataInitializerTest {
    @Mock
    private TestDataRepository testDataRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private RandomModelCreator randomModelCreator;

    @Mock
    private FileUtils fileUtils;

    private static TestDataInitializer testDataInitializer;

    @BeforeEach
    void setUp() {
        testDataInitializer = new TestDataInitializer(testDataRepository, studentService, randomModelCreator,
                fileUtils);
    }

    @Test
    void Method_initTestDada_shouldCallPostTestRecordsMethodFromTestDataRepository() {
        // given
        var students = List.of(
                new Student(0, null, "John", "Doe", null),
                new Student(0, null, "Jane", "Doe", null));
        var groups = List.of(
                new Group(0, "G1", null),
                new Group(0, "G2", null));
        var courses = List.of(
                new Course(0, "C1", "Some description 1", null),
                new Course(0, "C2", "Some description 2", null));

        // when
        when(studentService.getAll()).thenReturn(List.of());
        when(randomModelCreator.getGroups(anyList())).thenReturn(groups);
        when(randomModelCreator.getCourses(anyList())).thenReturn(courses);
        when(randomModelCreator.getStudents(anyList(), anyList(), anyInt())).thenReturn(students);

        testDataInitializer.initTestDada();

        // then
        verify(testDataRepository).postTestRecords(students, courses, groups);
    }
}
