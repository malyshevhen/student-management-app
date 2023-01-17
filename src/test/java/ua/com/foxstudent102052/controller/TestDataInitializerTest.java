package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.QueryPostService;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestDataInitializerTest {

    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @Mock
    private QueryPostService queryPostService;

    private static TestDataInitializer testDataInitializer;

    @BeforeEach
    void setUp() {
        var fileUtils = new FileUtils();
        testDataInitializer = new TestDataInitializer(studentService, courseService, groupService, queryPostService,
            fileUtils);
    }

    @Test
    void Method_initTestDada_shouldPostToDB_200Students() {
        testDataInitializer.initTestDada();
        verify(studentService, times(200)).addStudent(any(StudentDto.class));
    }

    @Test
    void Method_initTestDada_shouldPostToDB_10Courses() {
        testDataInitializer.initTestDada();
        verify(courseService, times(10)).addCourse(any(CourseDto.class));
    }

    @Test
    void Method_initTestDada_shouldPostToDB_10Groups() {
        testDataInitializer.initTestDada();
        verify(groupService, times(10)).addGroup(any(GroupDto.class));
    }

    @Test
    void Method_initTestDada_shouldPostToDB_StudentCourseRelations() {
        testDataInitializer.initTestDada();
        verify(studentService, atLeast(200)).addStudentToCourse(anyInt(), anyInt());
    }
}
