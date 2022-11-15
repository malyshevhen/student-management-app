package ua.com.foxstudent102052.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.StudentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {
    private static StudentRepository studentRepository;
    private static StudentService studentService;
    
    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentServiceImpl(studentRepository);
    }

    @Test
    void canAddStudent() {
        var student = new Student(1,"Dart", "Vader");
        doNothing().when(studentRepository).addStudent(student);
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(new Student());
        
        studentService.addStudent(student);
        
        verify(studentRepository, times(1)).addStudent(student);
    }

    @Test
    void canRemoveStudent() {
        var student = new Student(1,1,"Dart", "Vader");
        doNothing().when(studentRepository).removeStudent(student.getStudentId());
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.removeStudent(student.getStudentId());
        
        verify(studentRepository, times(1)).removeStudent(student.getStudentId());
    }

    @Test
    void canUpdateStudentFirstName() {
        var student = new Student(1,1,"Dart", "Vader");
        doNothing().when(studentRepository).updateStudentFirstName(student.getStudentId(), student.getFirstName());
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.updateStudentFirstName(student.getStudentId(), student.getFirstName());
        
        verify(studentRepository, times(1)).updateStudentFirstName(student.getStudentId(), student.getFirstName());
    }

    @Test
    void canUpdateStudentLastName() {
        var student = new Student(1,1,"Dart", "Vader");
        doNothing().when(studentRepository).updateStudentLastName(student.getStudentId(), student.getLastName());
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.updateStudentLastName(student.getStudentId(), student.getLastName());
        
        verify(studentRepository, times(1)).updateStudentLastName(student.getStudentId(), student.getLastName());
    }

    @Test
    void canUpdateStudentGroup() {
        var student = new Student(1,1,"Dart", "Vader");
        doNothing().when(studentRepository).updateStudentGroup(student.getStudentId(), student.getGroupId());
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.updateStudentGroup(student.getStudentId(), student.getGroupId());
        
        verify(studentRepository, times(1)).updateStudentGroup(student.getStudentId(), student.getGroupId());
    }

    @Test
    void canUpdateStudent() {
        var student = new Student(1,1,"Dart", "Vader");
        doNothing().when(studentRepository).updateStudent(student);
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.updateStudent(student);
        
        verify(studentRepository, times(1)).updateStudent(student);
    }

    @Test
    void canGetAllStudents() {
        var student = new Student(1,1,"Dart", "Vader");
        when(studentRepository.getAllStudents()).thenReturn(List.of(student));
        
        studentService.getAllStudents();
        
        verify(studentRepository, times(2)).getAllStudents();
    }

    @Test
    void canGetStudentById() {
        var expected = new Student(1,1,"Dart", "Vader");
        when(studentRepository.getStudentById(expected.getStudentId())).thenReturn(expected);
        
        var actual = studentService.getStudentById(expected.getStudentId());
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsByLastName() {
        var expected = new Student(1,1,"Dart", "Vader");
        when(studentRepository.getStudentsByLastName(expected.getLastName())).thenReturn(List.of(expected));
        
        var actual = studentService.getStudentsByLastName(expected.getLastName());
        
        assertEquals(List.of(expected), actual);
    }

    @Test
    void canGetStudentsByName() {
        var expected = List.of(new Student(1,1,"Dart", "Vader"));
        
        when(studentRepository.getStudentsByFirstName(new Student(1,1,"Dart", "Vader").getFirstName())).thenReturn(expected);
        
        var actual = studentService.getStudentsByName(new Student(1,1,"Dart", "Vader").getFirstName());
        
        assertEquals(expected, actual);
    }

    @Test
    void canGetStudentsBySurnameAndName() {
        Student student = new Student(1, 1, "Dart", "Vader");
        var expected = List.of(student);
        
        String lastName = student.getLastName();
        String firstName = student.getFirstName();
        
        when(studentRepository.getStudentsByFullName(lastName, firstName)).thenReturn(expected);
        
        var actual = studentService.getStudentsByFullName(lastName, firstName);
        
        assertEquals(expected, actual);
    }

    @Test
    void canAddStudentToCourse() {
        var student = new Student(1,1,"Dart", "Vader");
        var courseId = 1;
        doNothing().when(studentRepository).addStudentToCourse(student.getStudentId(), courseId);
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.addStudentToCourse(student.getStudentId(), courseId);
        
        verify(studentRepository, times(1)).addStudentToCourse(student.getStudentId(), courseId);
    }

    @Test
    void canRemoveStudentFromCourse() {
        var student = new Student(1,1,"Dart", "Vader");
        var courseId = 1;
        doNothing().when(studentRepository).removeStudentFromCourse(student.getStudentId(), courseId);
        when(studentRepository.getStudentById(student.getStudentId())).thenReturn(student);
        
        studentService.removeStudentFromCourse(student.getStudentId(), courseId);
        
        verify(studentRepository, times(1)).removeStudentFromCourse(student.getStudentId(), courseId);
    }

    @Test
    void canGetCoursesByStudentId() {
        var student = new Student(1,1,"Dart", "Vader");
        var expected = List.of(new Course(1, "Math", "Mathematics"));
        when(studentRepository.getCoursesByStudentId(student.getStudentId())).thenReturn(expected);
        
        var actual = studentService.getCoursesByStudentId(student.getStudentId());
        
        assertEquals(expected, actual);
    }
}
