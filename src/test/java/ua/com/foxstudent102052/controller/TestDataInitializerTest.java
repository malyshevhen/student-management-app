package ua.com.foxstudent102052.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.QueryPostService;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TestDataInitializerTest {
    private static GroupService groupService;
    private static StudentService studentService;
    private static CourseService courseService;
    private static TestDataInitializer testDataInitializer;

    @BeforeEach
    void setUp() {
        var fileUtils = new FileUtils();
        var queryPostService = mock(QueryPostService.class);
        groupService = mock(GroupService.class);
        studentService = mock(StudentService.class);
        courseService = mock(CourseService.class);
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
