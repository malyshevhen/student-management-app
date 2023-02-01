package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {
    private final StudentService studentService;

    public void addStudent(StudentDto studentDto) throws DataAccessException {
        studentService.addStudent(studentDto);
    }

    public void removeStudent(int studentId) throws NoSuchElementException, DataAccessException {
        studentService.removeStudent(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) throws NoSuchElementException, DataAccessException {
        studentService.addStudentToCourse(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId)
            throws NoSuchElementException, DataAccessException {
        studentService.removeStudentFromCourse(studentId, courseId);
    }

    public List<StudentDto> getAllStudents() throws NoSuchElementException, DataAccessException {
        return studentService.getAll();
    }

    public List<StudentDto> getStudents(String studentName, Integer courseId)
            throws NoSuchElementException, DataAccessException {
        return studentService.getStudentsByNameAndCourse(studentName, courseId);
    }
}
