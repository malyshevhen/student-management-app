package ua.com.foxstudent102052.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
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

import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;
import ua.com.foxstudent102052.utils.FileUtils;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@ExtendWith(MockitoExtension.class)
class TestDataInitializerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    @Mock
    private GroupService groupService;

    @Mock
    private QueryPostService queryPostService;

    @Mock
    private RandomModelCreator randomModelCreator;

    private static TestDataInitializer testDataInitializer;

    @BeforeEach
    void setUp() {
        var fileUtils = new FileUtils();

        testDataInitializer = new TestDataInitializer(studentService, courseService, groupService, queryPostService,
                randomModelCreator, fileUtils);
    }

    @Test
    void Method_initTestDada_shouldPostToDB_200Students() {
        // given
        var tenStudents = List.of(
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto(),
                new StudentDto());

        // when
        when(randomModelCreator.getStudents(anyList(), anyList(), anyList(), anyInt())).thenReturn(tenStudents);

        // then
        testDataInitializer.initTestDada();
        verify(studentService, times(tenStudents.size())).addStudent(any(StudentDto.class));
    }

    @Test
    void Method_initTestDada_shouldPostToDB_10Courses() {
        // given
        var tenCourses = List.of(
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto(),
                new CourseDto());

        // when
        when(randomModelCreator.getCourses(anyList())).thenReturn(tenCourses);

        // then
        testDataInitializer.initTestDada();
        verify(courseService, times(tenCourses.size())).addCourse(any(CourseDto.class));
    }

    @Test
    void Method_initTestDada_shouldPostToDB_11Groups() {
        // given
        var tenGroups = List.of(
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto(),
                new GroupDto());

        // when
        when(randomModelCreator.getGroups(anyList())).thenReturn(tenGroups);

        // then
        testDataInitializer.initTestDada();
        verify(groupService, times(tenGroups.size())).addGroup(any(GroupDto.class));
    }

    @Test
    void Method_initTestDada_shouldPostToDB_StudentCourseRelations() {
        // given
        var fifteenRelations = new HashMap<Integer, Set<Integer>>();
        fifteenRelations.put(1, Set.of(1, 2, 3, 4, 5));
        fifteenRelations.put(2, Set.of(6, 7, 8, 9, 10));
        fifteenRelations.put(3, Set.of(11, 12, 13, 14, 15));

        // when
        when(randomModelCreator.getStudentsCoursesRelations(anyList(), anyList(), anyInt()))
                .thenReturn(fifteenRelations);

        // then
        testDataInitializer.initTestDada();
        verify(studentService, atLeast(15)).addStudentToCourse(anyInt(), anyInt());
    }
}
