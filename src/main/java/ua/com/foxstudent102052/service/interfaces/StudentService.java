package ua.com.foxstudent102052.service.interfaces;

import java.util.List;

import ua.com.foxstudent102052.model.dto.StudentDto;

public interface StudentService {
    void addStudent(StudentDto studentDto);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudent(int id);

    void removeStudentFromCourse(int studentId, int groupId);

    List<StudentDto> getStudents();

    List<StudentDto> getStudentsByCourse(int courseId);

    List<StudentDto> getStudentsByGroup(int groupId);

    List<StudentDto> getStudents(String studentName, Integer courseId);
}
