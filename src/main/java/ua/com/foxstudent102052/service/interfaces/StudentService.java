package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.model.dto.StudentDto;

import java.util.List;

public interface StudentService {
    void addStudent(StudentDto studentDto);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudent(int id);

    void removeStudentFromCourse(int studentId, int groupId);

    List<StudentDto> getStudents();

    List<StudentDto> getStudents(int courseId);

    List<StudentDto> getStudents(String studentName, Integer courseId);
}
