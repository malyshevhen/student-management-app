package ua.com.foxstudent102052.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.CourseRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourseServiceImplTest {

    private CourseRepository courseRepository;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        courseService = new CourseServiceImpl(courseRepository);
    }
    
    @DisplayName("Method addCourse should pass Course to Repository")    
    @Test
    void addCourse() {
        doNothing().when(courseRepository).addCourse(new Course());
        when(courseRepository.getCourseByName("Java")).thenReturn(new Course());
        
        courseService.addCourse(new Course("Java", "Java course"));
        
        verify(courseRepository, times(1)).addCourse(new Course("Java", "Java course"));
    }

    @DisplayName("Method removeCourse should pass Course to Repository")
    @Test
    void removeCourse() {
        var course = new Course(1,"Java", "Java course");
        doNothing().when(courseRepository).removeCourse(course.getCourseId());
        when(courseRepository.getCourseById(course.getCourseId())).thenReturn(course);
        
        courseService.removeCourse(course.getCourseId());
        
        verify(courseRepository, times(1)).removeCourse(course.getCourseId());
    }

    @DisplayName("Method updateCourse should pass Course to Repository")
    @Test
    void updateCourse() {
        Course course = new Course(1, "Java", "Java course");
        doNothing().when(courseRepository).updateCourse(course);
        when(courseRepository.getCourseById(course.getCourseId())).thenReturn(course);
        
        courseService.updateCourse(course);
        
        verify(courseRepository, times(1)).updateCourse(course);
    }

    @DisplayName("Method updateCourseName should pass Course to Repository")
    @Test
    void updateCourseName() {
        var course = new Course(1, "Java", "Java course");
        doNothing().when(courseRepository).updateCourseName(course.getCourseId(), course.getCourseName());
        when(courseRepository.getCourseById(course.getCourseId())).thenReturn(course);
        
        courseService.updateCourseName(course.getCourseId(), course.getCourseName());
        
        verify(courseRepository, times(1)).updateCourseName(course.getCourseId(), course.getCourseName());
    }
    
    @DisplayName("Method updateCourseName should pass Course to Repository")
    @Test
    void updateCourseDescription() {
        var course = new Course(1, "Java", "Java course");
        doNothing().when(courseRepository).updateCourseDescription(course.getCourseId(), course.getCourseDescription());
        when(courseRepository.getCourseById(course.getCourseId())).thenReturn(course);
        
        courseService.updateCourseDescription(course.getCourseId(), course.getCourseDescription());
        
        verify(courseRepository,times(1)).updateCourseDescription(course.getCourseId(), course.getCourseDescription());
    }

    @DisplayName("Method getCourseById should return List of Courses")
    @Test
    void getAllCourses() {
        var expected = List.of(new Course());
        when(courseRepository.getAllCourses()).thenReturn(expected);
        
        var actual = courseService.getAllCourses();
        
        assertEquals(expected, actual);
    }

    @DisplayName("Method getCourseById should return Course")
    @Test
    void getCourseById() {
        var expected = new Course(1, "Java", "Java course");
        when(courseRepository.getCourseById(expected.getCourseId())).thenReturn(expected);
                
        var actual = courseService.getCourseById(expected.getCourseId());
        
        assertEquals(expected, actual);
    }

    @DisplayName("Method getCourseByName should return Course")
    @Test
    void getCourseByName() {
        var expected = new Course(1, "Java", "Java course");
        when(courseRepository.getCourseByName(expected.getCourseName())).thenReturn(expected);
        
        var actual = courseService.getCourseByName(expected.getCourseName());
        
        assertEquals(expected, actual);
    }
    
    @DisplayName("Method getStudentsByCourse should return list of Students")
    @Test
    void getStudentsByCourse() {
        int id = 1;
        var expected = List.of(new Student());
        when(courseRepository.getStudentsByCourseId(id)).thenReturn(expected);
        
        var actual = courseService.getStudentsByCourse(id);
        
        assertEquals(expected, actual);
    }
}
