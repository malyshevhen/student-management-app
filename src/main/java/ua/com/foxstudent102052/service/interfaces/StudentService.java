package ua.com.foxstudent102052.service.interfaces;

import java.util.List;

import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Student;

public interface StudentService {
    StudentDto addStudent(StudentDto studentDto);

    void addStudentToCourse(Long studentId, Long courseId);

    void removeStudent(Long id);

    void removeStudentFromCourse(Long studentId, Long groupId);

    List<StudentDto> getAll();

    List<StudentDto> getStudentsByCourse(Long courseId);

    List<StudentDto> getStudentsByGroup(Long groupId);

    List<StudentDto> getStudentsByNameAndCourse(String studentName, Long courseId);

    Student getStudent(Long id);
}
