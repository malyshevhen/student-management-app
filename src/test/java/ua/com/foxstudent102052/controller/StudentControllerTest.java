package ua.com.foxstudent102052.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {
    private GroupService groupService;
    private CourseService courseService;
    private StudentService studentService;
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        groupService = mock(GroupService.class);
        courseService = mock(CourseService.class);

        studentController = new StudentController(studentService, groupService, courseService);
    }

    @DisplayName("Method addStudent() should pass the student to the StudentService")
    @Test
    void addStudent() {
        doNothing().when(studentService).addStudent(new Student());

        studentController.addStudent(new StudentDto());

        verify(studentService, times(1)).addStudent(new Student());
    }

    @DisplayName("Method removeStudent() should pass the student to the StudentService")
    @Test
    void removeStudent() {
        doNothing().when(studentService).removeStudent(1);

        studentController.removeStudent(1);

        verify(studentService, times(1)).removeStudent(1);
    }

    @DisplayName("Method addStudentToCourse() should pass the student to the StudentService")
    @Test
    void addStudentToCourse() {
        doNothing().when(studentService).addStudentToCourse(1, 1);

        studentController.addStudentToCourse(1, 1);

        verify(studentService, times(1)).addStudentToCourse(1, 1);
    }

    @DisplayName("Method removeStudentFromCourse() should pass the student to the StudentService")
    @Test
    void removeStudentFromCourse() {
        doNothing().when(studentService).removeStudentFromCourse(1, 1);

        studentController.removeStudentFromCourse(1, 1);

        verify(studentService, times(1)).removeStudentFromCourse(1, 1);
    }

    @DisplayName("Method updateStudentFirstName() should pass the student to the StudentService")
    @Test
    void updateStudentFirstName() {
        doNothing().when(studentService).updateStudentFirstName(1, "firstName");

        studentController.updateStudentFirstName(1, "firstName");

        verify(studentService, times(1)).updateStudentFirstName(1, "firstName");
    }

    @DisplayName("Method updateStudentLastName() should pass the student to the StudentService")
    @Test
    void updateStudentsLastName() {
        doNothing().when(studentService).updateStudentLastName(1, "lastName");

        studentController.updateStudentsLastName(1, "lastName");

        verify(studentService, times(1)).updateStudentLastName(1, "lastName");
    }

    @DisplayName("Method updateStudentGroup() should pass the student to the StudentService")
    @Test
    void updateStudentsGroup() {
        doNothing().when(studentService).updateStudentGroup(1, 1);

        studentController.updateStudentsGroup(1, 1);

        verify(studentService, times(1)).updateStudentGroup(1, 1);
    }

    @DisplayName("Method updateStudentCourse() should pass the student to the StudentService")
    @Test
    void updateStudent() {
        doNothing().when(studentService).updateStudent(new Student());

        studentController.updateStudent(new StudentDto());

        verify(studentService, times(1)).updateStudent(new Student());
    }

    @DisplayName("Method getStudentById() should return the studentDto")
    @Test
    void getStudentById() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(studentService.getStudentById(1)).thenReturn(student);
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());
        
        var actual = studentController.getStudentById(1);
        var expected = new StudentDto(1, 1,"", "firstName", "Lastname", List.of());
                
        assertEquals(expected, actual);        
    }

    @DisplayName("Method getAllStudents() should return the list of studentDto")
    @Test
    void getAllStudents() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(studentService.getAllStudents()).thenReturn(List.of(student));
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());

        var actual = studentController.getAllStudents();
        var expected = List.of(new StudentDto(1, 1,"", "firstName", "Lastname", List.of()));

        assertEquals(expected, actual);
    }

    @DisplayName("Method getAllStudentsByGroupId() should return the list of studentDto")
    @Test
    void getStudentsByGroupId() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(groupService.getStudentsByGroup(1)).thenReturn(List.of(student));
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());

        var actual = studentController.getStudentsByGroupId(1);
        var expected = List.of(new StudentDto(1, 1,"", "firstName", "Lastname", List.of()));

        assertEquals(expected, actual);
    }

    @DisplayName("Method getAllStudentsByCourseId() should return the list of studentDto")
    @Test
    void getStudentsByLastName() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(studentService.getStudentsByLastName("Lastname")).thenReturn(List.of(student));
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());

        var actual = studentController.getStudentsByLastName("Lastname");
        var expected = List.of(new StudentDto(1, 1,"", "firstName", "Lastname", List.of()));

        assertEquals(expected, actual);
    }

    @DisplayName("Method getAllStudentsByCourseId() should return the list of studentDto")
    @Test
    void getStudentsByName() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(studentService.getStudentsByName("firstName")).thenReturn(List.of(student));
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());

        var actual = studentController.getStudentsByName("firstName");
        var expected = List.of(new StudentDto(1, 1,"", "firstName", "Lastname", List.of()));

        assertEquals(expected, actual);
    }

    @DisplayName("Method getAllStudentsByCourseId() should return the list of studentDto")
    @Test
    void getStudentsBySurnameAndName() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(studentService.getStudentsBySurnameAndName("firstName", "Lastname")).thenReturn(List.of(student));
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());

        var actual = studentController.getStudentsBySurnameAndName("firstName", "Lastname");
        var expected = List.of(new StudentDto(1, 1,"", "firstName", "Lastname", List.of()));

        assertEquals(expected, actual);
    }

    @DisplayName("Method getStudentsByCourseId() should return the list of studentDto")
    @Test
    void getStudentsByCourseId() {
        var student = new Student(1, 1, "firstName", "Lastname");
        when(courseService.getStudentsByCourse(1)).thenReturn(List.of(student));
        when(groupService.getGroupById(1)).thenReturn(new Group(1, ""));
        when(studentService.getCoursesByStudentId(1)).thenReturn(List.of());

        var actual = studentController.getStudentsByCourseId(1);
        var expected = List.of(new StudentDto(1, 1,"", "firstName", "Lastname", List.of()));

        assertEquals(expected, actual);
    }
}
