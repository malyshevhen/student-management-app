package ua.com.foxstudent102052.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.StudentService;

class StudentServiceImplTest {
    private final ModelMapper modelMapper = new ModelMapper();
    private StudentDao studentDao;
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentDao = mock(StudentDao.class);
        studentService = new StudentServiceImpl(studentDao);
    }

    @Test
    void MethodAddStudent_ShouldPassNewStudentToRepository() throws ElementAlreadyExistException, DAOException {
        // given
        var studentDto = StudentDto.builder()
            .firstName("John")
            .lastName("Doe")
            .build();
        var student = modelMapper.map(studentDto, Student.class);

        // when
        studentService.addStudent(studentDto);

        // then
        verify(studentDao).addStudent(student);
    }

    @Test
    void MethodRemoveStudent_ShouldRemoveExistingStudentFromDb() throws DAOException, ElementAlreadyExistException {
        // given
        var student = Student.builder().id(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(1)).thenReturn(Optional.of(student)).thenThrow(DAOException.class);
        doNothing().when(studentDao).removeStudent(student.getId());

        // then
        studentService.removeStudent(student.getId());

        verify(studentDao).removeStudent(student.getId());
    }

    @Test
    void MethodRemoveStudent_ShouldThrowAnException_IfStudentDoesNotExist() throws DAOException {
        // given
        var student = Student.builder().id(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(1)).thenReturn(Optional.empty());
        doNothing().when(studentDao).removeStudent(anyInt());

        // then
        assertThrows(NoSuchElementException.class, () -> studentService.removeStudent(student.getId()),
            "Student wasn`t removed");
    }

    @Test
    void MethodGetStudents_ShouldSendRequestToDao() throws ElementAlreadyExistException, DAOException {
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
        throws ElementAlreadyExistException, DAOException {
        // given
        Student newStudent = Student.builder().id(1).firstName("John").lastName("Doe").build();

        // when
        when(studentDao.getStudent(newStudent.getId())).thenReturn(Optional.of(newStudent));
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
        throws ElementAlreadyExistException, DAOException {
        // given
        int studentId = 1;
        int groupId = 1;
        var student = new Student(studentId, groupId, "", "");

        // when
        when(studentDao.getStudentsByCourse(groupId)).thenReturn(List.of(student));
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
    void MethodGetStudentsByCourseShould_ReturnListOfStudents_ByCourseId() throws ElementAlreadyExistException, DAOException {
        // when
        when(studentDao.getStudentsByCourse(anyInt())).thenReturn(List.of(new Student(0, 0, "", "")));
        studentService.getStudentsByCourse(1);

        // then
        verify(studentDao).getStudentsByCourse(1);
    }

    @Test
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents() throws DAOException, ElementAlreadyExistException {
        // when
        when(studentDao.getStudents(anyString(), anyInt()))
            .thenReturn(List.of(new Student(0, 0, "", "")));
        studentService.getStudents("John", 1);

        // then
        verify(studentDao).getStudents("John", 1);
    }

    @Test
    void MethodGetStudentsByGropShould_ReturnListOfStudents_ByCourseId() throws ElementAlreadyExistException, DAOException {
        // when
        when(studentDao.getStudentsByGroup(anyInt())).thenReturn(List.of(new Student(0, 0, "", "")));
        studentService.getStudentsByGroup(1);

        // then
        verify(studentDao).getStudentsByGroup(1);
    }
}
