package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.dto.StudentDto;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.StudentRepository;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentServiceImpl(studentRepository);
    }

    @Test
    void MethodAddStudent_ShouldPassNewStudentToRepository() throws ServiceException, RepositoryException {
        // given
        var studentDto = StudentDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build();
        var student = Student.builder()
                .studentId(1)
                .firstName("firstName")
                .lastName("lastName")
                .build();

        // when
        when(studentRepository.getLastStudent()).thenReturn(student);

        // then
        studentService.addStudent(studentDto);
        verify(studentRepository).addStudent(student);
    }

    @Test
    void MethodAddStudent_ShouldThrowAnException_IfCheckIsFailed() throws RepositoryException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doNothing().when(studentRepository).addStudent(student);
        when(studentRepository.getLastStudent()).thenReturn(new Student());

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudent(StudentMapper.toStudentDto(student)),
                "Student wasn`t added");
    }

    @Test
    void MethodAddStudentShould_ThrowAnException_IfRepositoryThrowsAnException() throws RepositoryException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doThrow(RepositoryException.class).when(studentRepository).addStudent(student);

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudent(StudentMapper.toStudentDto(student)),
                "Student wasn`t added");
    }

    @Test
    void MethodRemoveStudent_ShouldRemoveExistingStudentFromDb() throws RepositoryException, ServiceException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentRepository.getStudentById(1)).thenReturn(student).thenThrow(RepositoryException.class);
        doNothing().when(studentRepository).removeStudent(student.getStudentId());

        // then
        studentService.removeStudent(student.getStudentId());

        verify(studentRepository).removeStudent(student.getStudentId());
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfStudentDoesNotExist() throws RepositoryException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentRepository.getStudentById(1)).thenReturn(null);

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudent(student.getStudentId()),
                "Student wasn`t removed");
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfRepositoryThrowsAnException() throws RepositoryException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentRepository.getStudentById(1)).thenReturn(student);
        doThrow(RepositoryException.class).when(studentRepository).removeStudent(student.getStudentId());

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudent(student.getStudentId()),
                "Student wasn`t removed");
    }

    @Test
    void MethodGetAllStudents_ShouldReturnListOfAllStudents() throws ServiceException, RepositoryException {
        // when
        studentService.getStudents();

        // then
        verify(studentRepository).getAllStudents();

    }

    @Test
    void MethodGetAllStudents_ShouldThrowAnException_WhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Students doesn't exist")).when(studentRepository).getAllStudents();

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudents(),
                "Students doesn't exist");
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfStudentWasNotRemoved() throws RepositoryException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentRepository.getStudentById(1)).thenReturn(student).thenReturn(student);
        doNothing().when(studentRepository).removeStudent(student.getStudentId());

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudent(student.getStudentId()),
                "Student wasn`t removed");
    }

    @Test
    void MethodAddStudentToCourse_ShouldAddExistingStudentToExistingCourse()
            throws ServiceException, RepositoryException {
        // given
        Student newStudent = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentRepository.getStudentsByCourseId(newStudent.getStudentId())).thenReturn(List.of(newStudent));
        studentService.addStudentToCourse(1, 1);

        // then
        verify(studentRepository).addStudentToCourse(1, 1);
    }

    @Test
    void MethodAddStudentToCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Student with id 1 doesn't exist")).when(studentRepository)
                .addStudentToCourse(1, 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudentToCourse(1, 1),
                "Student with id 1 doesn't exist");
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldRemoveExistingStudentFromExistingCourse()
            throws ServiceException, RepositoryException {
        // when
        when(studentRepository.getStudentsByCourseId(anyInt())).thenReturn(List.of(new Student()));
        studentService.removeStudentFromCourse(1, 1);

        // then
        verify(studentRepository).removeStudentFromCourse(1, 1);
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Student with id 1 doesn't exist")).when(studentRepository)
                .removeStudentFromCourse(1, 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudentFromCourse(1, 1),
                "Student with id 1 doesn't exist");
    }

    @Test
    void MethodGetStudentsByCourseShould_ReturnListOfStudents_ByCourseId() throws ServiceException, RepositoryException {
        // when
        when(studentRepository.getStudentsByCourseId(anyInt())).thenReturn(List.of(new Student()));
        studentService.getStudents(1);

        // then
        verify(studentRepository).getStudentsByCourseId(1);
    }

    @Test
    void MethodGetStudentsByCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Students doesn't exist")).when(studentRepository).getStudentsByCourseId(1);

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudents(1),
                "Students doesn't exist");
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents() throws RepositoryException, ServiceException {
        // when
        when(studentRepository.getStudentsByNameAndCourse(anyString(), anyInt()))
                .thenReturn(List.of(new Student()));
        studentService.getStudents("John", 1);

        // then
        verify(studentRepository).getStudentsByNameAndCourse("John", 1);
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Students doesn't exist")).when(studentRepository)
                .getStudentsByNameAndCourse("John", 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudents("John", 1),
                "Students doesn't exist");
    }

    @Test
    void MethodIfExist_ShouldReturnTrue_IfStudentExist() throws RepositoryException, ServiceException {
        // when
        when(studentRepository.getStudentById(1)).thenReturn(Student.builder().build());

        // then
        assertTrue(studentService.ifExist(1));
    }

    @Test
    void MethodIfExist_ShouldReturnFalse_IfStudentDoesNotExist() throws RepositoryException, ServiceException {
        // when
        doThrow(new RepositoryException("Student with id 1 doesn't exist")).when(studentRepository).getStudentById(1);

        // then
        assertFalse(studentService.ifExist(1));
    }
}
