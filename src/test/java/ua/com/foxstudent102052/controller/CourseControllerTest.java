package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourseControllerTest {
    private CourseService courseService;
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        courseService = mock(CourseService.class);
        courseController = new CourseController(courseService);
    }
   
    @DisplayName("Method addCourse() should pass adding command to CourseService")
    @ParameterizedTest
    @MethodSource
    void addCourse(int courseId, String courseName, String courseDescription) {
        Course course = new Course(courseId, courseName, courseDescription);
        doNothing().when(courseService).addCourse(course);
        
        courseController.addCourse(courseName, courseDescription);
        
        verify(courseService, times(1)).addCourse(course);
    }
    
    private static Stream<Arguments> addCourse() {
        return Stream.of(
                Arguments.of(0, "Java", "Java course"),
                Arguments.of(0, "C++", "C++ course"),
                Arguments.of(0, "C#", "C# course")
        );
    }

    @DisplayName("Should catch IllegalArgumentException")
    @ParameterizedTest
    @MethodSource
    void addCourseTest2(String courseName, String courseDescription) {
        doThrow(IllegalArgumentException.class).when(courseService).addCourse(any(Course.class));

        courseController.addCourse(courseName, courseDescription);

        verify(courseService, times(1)).addCourse(any(Course.class));
    }
    
    private static Stream<Arguments> addCourseTest2() {
        return Stream.of(
                Arguments.of(null, "Java course"),
                Arguments.of("Java", null),
                Arguments.of(null, null)
        );
    }

    @DisplayName("Method updateCourse() should pass updating command to CourseService")
    @ParameterizedTest
    @MethodSource
    void updateCourse(CourseDto courseDto) {
        Course course = new Course(courseDto.getId(), courseDto.getName(), courseDto.getDescription());
        doNothing().when(courseService).updateCourse(course);
        
        courseController.updateCourse(courseDto);
        
        verify(courseService, times(1)).updateCourse(course);
    }
    
    private static Stream<Arguments> updateCourse() {
        return Stream.of(
                Arguments.of(new CourseDto(1, "Java", "Java course")),
                Arguments.of(new CourseDto(2, "C++", "C++ course")),
                Arguments.of(new CourseDto(3, "C#", "C# course"))
        );
    }

    @DisplayName("Method removeCourse() should pass Updating command to CourseService")
    @ParameterizedTest
    @MethodSource
    void updateCourseName(int courseId, String courseName) {
        doNothing().when(courseService).updateCourseName(courseId, courseName);
        
        courseController.updateCourseName(courseId, courseName);
        
        verify(courseService, times(1)).updateCourseName(courseId, courseName);
    }
    
    private static Stream<Arguments> updateCourseName() {
        return Stream.of(
                Arguments.of(1, "Java"),
                Arguments.of(2, "C++"),
                Arguments.of(3, "C#")
        );
    }

    @DisplayName("Method removeCourse() should pass Updating command to CourseService")
    @ParameterizedTest
    @MethodSource
    void updateCourseDescription(int courseId, String courseDescription) {
        doNothing().when(courseService).updateCourseDescription(courseId, courseDescription);
        
        courseController.updateCourseDescription(courseId, courseDescription);
        
        verify(courseService, times(1)).updateCourseDescription(courseId, courseDescription);
    }
    
    private static Stream<Arguments> updateCourseDescription() {
        return Stream.of(
                Arguments.of(1, "Java course"),
                Arguments.of(2, "C++ course"),
                Arguments.of(3, "C# course")
        );
    }

    @DisplayName("Method removeCourse() should pass removing command to CourseService")
    @ParameterizedTest
    @MethodSource
    void removeCourse(int courseId) {
        doNothing().when(courseService).removeCourse(courseId);
        
        courseController.removeCourse(courseId);
        
        verify(courseService, times(1)).removeCourse(courseId);
    }
    
    private static Stream<Arguments> removeCourse() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3)
        );
    }

    @DisplayName("Method 'getCourseById' should return 'CourseDto' instance by id")
    @ParameterizedTest
    @MethodSource
    void getCourseByIdTest(int courseId, Course course, List<Student> students, CourseDto expected) {
        when(courseService.getCourseById(courseId)).thenReturn(course);
        when(courseService.getStudentsByCourse(courseId)).thenReturn(students);
        
        CourseDto actual = courseController.getCourseById(courseId);
        
        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> getCourseByIdTest() {
        return Stream.of(
                Arguments.of(
                    1,
                    new Course(1, "Java", "Java course"),
                    List.of(new Student(1,1, "Ivan", "Ivanenko")),
                    new CourseDto(1, "Java", "Java course",
                        List.of(new StudentDto(1,1, "", "Ivan", "Ivanenko", List.of())))),
                Arguments.of(
                    2,
                    new Course(2, "C++", "C++ course"),
                    List.of(new Student(2, 2, "Petr", "Petrenko")),
                    new CourseDto(2, "C++", "C++ course",
                        List.of(new StudentDto(2, 2, "", "Petr", "Petrenko", List.of()))))
        );
    }

    @DisplayName("Method 'getAllCourses' should return list of 'CourseDto' instances")
    @ParameterizedTest
    @MethodSource
    void getAllCourses(List<Course> courses, List<Student> students, List<CourseDto> expected) {
        when(courseService.getAllCourses()).thenReturn(courses);
        when(courseService.getStudentsByCourse(1)).thenReturn(students);
        when(courseService.getStudentsByCourse(2)).thenReturn(students);
        
        List<CourseDto> actual = courseController.getAllCourses();
        
        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> getAllCourses() {
        return Stream.of(
                Arguments.of(
                    List.of(
                        new Course(1, "Java", "Java course"),
                        new Course(2, "C++", "C++ course")),
                    List.of(new Student(1,1, "Ivan", "Ivanenko")),
                    List.of(
                        new CourseDto(1, "Java", "Java course",
                            List.of(new StudentDto(1,1, "", "Ivan", "Ivanenko", List.of()))),
                        new CourseDto(2, "C++", "C++ course",
                            List.of(new StudentDto(1,1, "", "Ivan", "Ivanenko", List.of()))))),
                Arguments.of(
                    List.of(
                        new Course(2, "C++", "C++ course"),
                        new Course(1, "Java", "Java course")),
                    List.of(new Student(2, 2, "Petr", "Petrenko")),
                    List.of(
                        new CourseDto(2, "C++", "C++ course",
                            List.of(new StudentDto(2, 2, "", "Petr", "Petrenko", List.of()))),
                        new CourseDto(1, "Java", "Java course",
                            List.of(new StudentDto(2, 2, "", "Petr", "Petrenko", List.of())))))
        );
    }

    @DisplayName("Method 'getCourseByName' should return list of 'StudentDto' instances by Course Id")
    @ParameterizedTest
    @MethodSource
    void getStudentsByCourse(int courseId, List<Student> students, List<StudentDto> expected) {
        when(courseService.getStudentsByCourse(courseId)).thenReturn(students);
        
        List<StudentDto> actual = courseController.getStudentsByCourse(courseId);
        
        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> getStudentsByCourse() {
        return Stream.of(
                Arguments.of(
                    1,
                    List.of(new Student(1,1, "Ivan", "Ivanenko")),
                    List.of(new StudentDto(1,1, "", "Ivan", "Ivanenko", List.of()))),
                Arguments.of(
                    2,
                    List.of(new Student(2, 2, "Petr", "Petrenko")),
                    List.of(new StudentDto(2, 2, "", "Petr", "Petrenko", List.of())))
        );
    }

    @DisplayName("Method 'getCourseByName' should return 'CourseDto' instance by course name")
    @ParameterizedTest
    @MethodSource
    void getCourseByName(String courseName, Course course, List<Student> students, CourseDto expected) {
        when(courseService.getCourseByName(courseName)).thenReturn(course);
        when(courseService.getStudentsByCourse(course.getCourseId())).thenReturn(students);
        
        CourseDto actual = courseController.getCourseByName(courseName);
        
        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> getCourseByName() {
        return Stream.of(
                Arguments.of(
                    "Java",
                    new Course(1, "Java", "Java course"),
                    List.of(new Student(1,1, "Ivan", "Ivanenko")),
                    new CourseDto(1, "Java", "Java course",
                        List.of(new StudentDto(1,1, "", "Ivan", "Ivanenko", List.of())))),
                Arguments.of(
                    "C++",
                    new Course(2, "C++", "C++ course"),
                    List.of(new Student(2, 2, "Petr", "Petrenko")),
                    new CourseDto(2, "C++", "C++ course",
                        List.of(new StudentDto(2, 2, "", "Petr", "Petrenko", List.of()))))
        );
    }
}
