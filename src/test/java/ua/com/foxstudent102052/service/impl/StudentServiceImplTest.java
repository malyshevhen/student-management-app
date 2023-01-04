package ua.com.foxstudent102052.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.mapper.StudentModelMapper;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {
    private StudentDao studentDao;
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentDao = mock(StudentDao.class);
        studentService = new StudentServiceImpl(studentDao);
    }

    @Test
    void MethodAddStudent_ShouldPassNewStudentToRepository() throws ServiceException, DAOException {
        // given
        var studentDto = StudentDto.builder()
            .firstName("firstName")
            .lastName("lastName")
            .build();
        var student = StudentModelMapper.toStudent(studentDto);

        // when
        studentService.addStudent(studentDto);

        // then
        verify(studentDao).addStudent(student);
    }

    @Test
    void MethodAddStudent_ShouldThrowAnException_IfCheckIsFailed() throws DAOException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doThrow(DAOException.class).when(studentDao).addStudent(student);

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudent(StudentModelMapper.toStudentDto(student)),
            "Student wasn`t added");
    }

    @Test
    void MethodAddStudentShould_ThrowAnException_IfRepositoryThrowsAnException() throws DAOException {
        // given
        var student = Student.builder().firstName("John").lastName("Doe").build();

        // when
        doThrow(DAOException.class).when(studentDao).addStudent(student);

        // then
        assertThrows(ServiceException.class, () -> studentService.addStudent(StudentModelMapper.toStudentDto(student)),
            "Student wasn`t added");
    }

    @Test
    void MethodRemoveStudent_ShouldRemoveExistingStudentFromDb() throws DAOException, ServiceException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(1)).thenReturn(Optional.of(student)).thenThrow(DAOException.class);
        doNothing().when(studentDao).removeStudent(student.studentId());

        // then
        studentService.removeStudent(student.studentId());

        verify(studentDao).removeStudent(student.studentId());
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfStudentDoesNotExist() throws DAOException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(1)).thenReturn(Optional.empty());
        doNothing().when(studentDao).removeStudent(anyInt());

        // then
        assertThrows(NoSuchElementException.class, () -> studentService.removeStudent(student.studentId()),
            "Student wasn`t removed");
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfRepositoryThrowsAnException() throws DAOException {
        // given
        var student = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(1)).thenReturn(Optional.of(student));
        doThrow(DAOException.class).when(studentDao).removeStudent(student.studentId());

        // then
        assertThrows(ServiceException.class, () -> studentService.removeStudent(student.studentId()),
            "Student wasn`t removed");
    }

    @Test
    void MethodGetStudents_ShouldSendRequestToDao() throws ServiceException, DAOException {
        // given
        var students = List.of(
            new Student(1, 1, "John", "Doe")
        );

        // when
        when(studentDao.getStudents()).thenReturn(students);
        studentService.getStudents();

        // then
        verify(studentDao).getStudents();
    }

    @Test
    void MethodGetStudents_ShouldThrowAnException_WhenStudentsDoesNotExist() throws DAOException {
        // when
        when(studentDao.getStudents()).thenReturn(List.of());

        // then
        assertThrows(NoSuchElementException.class, () -> studentService.getStudents(),
            "Students doesn't exist");
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfStudentWasNotRemoved() throws DAOException {
        // given
        int studentId = 1;

        // when
        when(studentDao.getStudent(studentId)).thenReturn(Optional.empty());
        doNothing().when(studentDao).removeStudent(studentId);

        // then
        assertThrows(NoSuchElementException.class, () -> studentService.removeStudent(studentId),
            "Student wasn`t removed");
    }

    @Test
    void MethodAddStudentToCourse_ShouldAddExistingStudentToExistingCourse()
        throws ServiceException, DAOException {
        // given
        Student newStudent = Student.builder().studentId(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(newStudent.studentId())).thenReturn(Optional.of(newStudent));
        studentService.addStudentToCourse(1, 1);

        // then
        verify(studentDao).addStudentToCourse(1, 1);
    }

    @Test
    void MethodAddStudentToCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws DAOException {
        // when
        doThrow(new DAOException("Student with id 1 doesn't exist")).when(studentDao)
            .addStudentToCourse(1, 1);

        // then
        assertThrows(NoSuchElementException.class, () -> studentService.addStudentToCourse(1, 1),
            "Student with id 1 doesn't exist");
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldRemoveExistingStudentFromExistingCourse()
        throws ServiceException, DAOException {
        // given
        int studentId = 1;
        int groupId = 1;
        var student = new Student(studentId, groupId, "", "");

        // when
        when(studentDao.getStudents(groupId)).thenReturn(List.of(student));
        studentService.removeStudentFromCourse(studentId, groupId);

        // then
        verify(studentDao).removeStudentFromCourse(studentId, groupId);
    }

    @Test
    void MethodRemoveStudentFromCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws DAOException {
        // when
        doThrow(new DAOException("Student with id 1 doesn't exist")).when(studentDao)
            .removeStudentFromCourse(1, 1);

        // then
        assertThrows(NoSuchElementException.class, () -> studentService.removeStudentFromCourse(1, 1),
            "Student with id 1 doesn't exist");
    }

    @Test
    void MethodGetStudentsByCourseShould_ReturnListOfStudents_ByCourseId() throws ServiceException, DAOException {
        // when
        when(studentDao.getStudents(anyInt())).thenReturn(List.of(new Student(0, 0, "", "")));
        studentService.getStudents(1);

        // then
        verify(studentDao).getStudents(1);
    }

    @Test
    void MethodGetStudentsByCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws DAOException {
        // when
        doThrow(new DAOException("Students doesn't exist")).when(studentDao).getStudents(1);

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudents(1),
            "Students doesn't exist");
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents() throws DAOException, ServiceException {
        // when
        when(studentDao.getStudents(anyString(), anyInt()))
            .thenReturn(List.of(new Student(0, 0, "", "")));
        studentService.getStudents("John", 1);

        // then
        verify(studentDao).getStudents("John", 1);
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldThrowAnException_WhenStudentDoesNotExist() throws DAOException {
        // when
        doThrow(new DAOException("Students doesn't exist")).when(studentDao)
            .getStudents("John", 1);

        // then
        assertThrows(ServiceException.class, () -> studentService.getStudents("John", 1),
            "Students doesn't exist");
    }
}
