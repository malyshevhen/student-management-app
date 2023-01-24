package ua.com.foxstudent102052.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.dao.testinit.TestDataRepository;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@ExtendWith(MockitoExtension.class)
class TestDataInitializerTest {
    @Mock
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

    @Test
    void Method_initTestDada_shouldCallPostTestRecordsMethodFromTestDataRepository() {
        // given
        var students = List.of(
                new Student(0, 0, "John", "Doe"),
                new Student(0, 0, "Jane", "Doe"));
        var groups = List.of(
                new Group(0, "G1"),
                new Group(0, "G2"));
        var courses = List.of(
                new Course(0, "C1", "Some description 1"),
                new Course(0, "C2", "Some description 2"));

        // when
        when(randomModelCreator.getGroups(anyList())).thenReturn(groups);
        when(randomModelCreator.getCourses(anyList())).thenReturn(courses);
        when(randomModelCreator.getStudents(anyList(), anyList(), anyInt())).thenReturn(students);

        testDataInitializer.initTestDada();

        // then
        verify(testDataRepository).postTestRecords(students, courses, groups);
    }

    @Test
    void Method_initTestDada_shouldCallAddStudentToCourseMethodFromStudentService_atLeastFourTimes() {
        // given
        var relationMap = new HashMap<Integer, Set<Integer>>();
        relationMap.put(1, Set.of(1));
        relationMap.put(2, Set.of(2));
        relationMap.put(3, Set.of(3));
        relationMap.put(4, Set.of(4));

        // when
        when(randomModelCreator.getStudentsCoursesRelations(any(int[].class), any(int[].class), anyInt()))
                .thenReturn(relationMap);

        testDataInitializer.initTestDada();

        // then
        verify(studentService, times(4)).addStudentToCourse(anyInt(), anyInt());
    }
}
