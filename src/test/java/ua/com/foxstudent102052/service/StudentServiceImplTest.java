package ua.com.foxstudent102052.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.RepositoryException;
import ua.com.foxstudent102052.repository.StudentRepository;

public class StudentServiceImplTest {
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentServiceImpl(studentRepository);
    }

    @Test
    void MethodAddStudentShouldPassNewStudentToRepository() throws ServiceException, RepositoryException {
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
    void MethodAddStudentShouldThrowAnExceptionIfCheckIsFailed() throws RepositoryException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doNothing().when(studentRepository).addStudent(student);
        when(studentRepository.getLastStudent()).thenReturn(new Student());

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudent(StudentMapper.toDto(student)),
                "Student wasn`t added");
    }

    @Test
    void MethodAddStudentShouldThrowAnExceptionIfRepositoryThrowsAnException() throws RepositoryException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doThrow(RepositoryException.class).when(studentRepository).addStudent(student);

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudent(StudentMapper.toDto(student)),
                "Student wasn`t added");
    }

    @Test
    void MethodRemoveStudentShouldRemoveExistingStudentFromDb() throws RepositoryException, ServiceException {
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
    void MethodRemoveStudentShouldThrowAnExceptionIfStudentDoesNotExist() throws RepositoryException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentRepository.getStudentById(1)).thenReturn(null);

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudent(student.getStudentId()),
                "Student wasn`t removed");
    }

    @Test
    void MethodRemoveStudentShouldThrowAnExceptionIfRepositoryThrowsAnException() throws RepositoryException {
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
    void MethodGetAllStudentsShouldReturnListOfAllStudents() throws ServiceException, RepositoryException {
        // when
        studentService.getAllStudents();

        // then
        verify(studentRepository).getAllStudents();

    }

    @Test
    void MethodGetAllStudentsShouldThrowAnExceptionWhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Students doesn't exist")).when(studentRepository).getAllStudents();

        // then
        assertThrows(ServiceException.class, () -> studentService.getAllStudents(),
                "Students doesn't exist");
    }

    @Test
    void MethodRemoveStudentShouldThrowAnExceptionIfStudentWasNotRemoved() throws RepositoryException {
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
    void MethodAddStudentToCourseShouldAddExistingStudentToExistingCourse()
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
    void MethodAddStudentToCourseShouldThrowAnExceptionWhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Student with id 1 doesn't exist")).when(studentRepository)
                .addStudentToCourse(1, 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudentToCourse(1, 1),
                "Student with id 1 doesn't exist");
    }

    @Test
    void MethodRemoveStudentFromCourseShouldRemoveExistingStudentFromExistingCourse()
            throws ServiceException, RepositoryException {
        // when
        when(studentRepository.getStudentsByCourseId(anyInt())).thenReturn(List.of(new Student()));
        studentService.removeStudentFromCourse(1, 1);

        // then
        verify(studentRepository).removeStudentFromCourse(1, 1);
    }

    @Test
    void MethodRemoveStudentFromCourseShouldThrowAnExceptionWhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Student with id 1 doesn't exist")).when(studentRepository)
                .removeStudentFromCourse(1, 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudentFromCourse(1, 1),
                "Student with id 1 doesn't exist");
    }

    @Test
    void MethodGetStudentsByCourseShouldReturnListOfStudentsByCourseId() throws ServiceException, RepositoryException {
        // when
        when(studentRepository.getStudentsByCourseId(anyInt())).thenReturn(List.of(new Student()));
        studentService.getStudentsByCourse(1);

        // then
        verify(studentRepository).getStudentsByCourseId(1);
    }

    @Test
    void MethodGetStudentsByCourseShouldThrowAnExceptionWhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Students doesn't exist")).when(studentRepository).getStudentsByCourseId(1);

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudentsByCourse(1),
                "Students doesn't exist");
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldReturnListOfStudents() throws RepositoryException, ServiceException {
        // when
        when(studentRepository.getStudentsByNameAndCourse(anyString(), anyInt()))
                .thenReturn(List.of(new Student()));
        studentService.getStudentsByNameAndCourse("John", 1);

        // then
        verify(studentRepository).getStudentsByNameAndCourse("John", 1);
    }

    @Test
    void MethodGetStudentsByNameAndCourseShouldThrowAnExceptionWhenStudentDoesNotExist() throws RepositoryException {
        // when
        doThrow(new RepositoryException("Students doesn't exist")).when(studentRepository)
                .getStudentsByNameAndCourse("John", 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudentsByNameAndCourse("John", 1),
                "Students doesn't exist");
    }

    @Test
    void MethodIfExistShouldReturnTrueIfStudentExist() throws RepositoryException, ServiceException {
        // when
        when(studentRepository.getStudentById(1)).thenReturn(Student.builder().build());

        // then
        assertTrue(studentService.ifExist(1));
    }

    @Test
    void MethodIfExistShouldReturnFalseIfStudentDoesNotExist() throws RepositoryException, ServiceException {
        // when
        doThrow(new RepositoryException("Student with id 1 doesn't exist")).when(studentRepository).getStudentById(1);

        // then
        assertFalse(studentService.ifExist(1));
    }
}
