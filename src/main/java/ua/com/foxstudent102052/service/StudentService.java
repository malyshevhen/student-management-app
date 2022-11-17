package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

public interface StudentService {
    void addStudent(StudentDto studentDto);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudent(int id);

    void removeStudentFromCourse(int studentId, int groupId);

    List<StudentDto> getAllStudents();

    List<StudentDto> getStudentsByCourse(int courseId);

    List<StudentDto> getStudentsByCourseNameAndGroupId(String studentName, Integer courseId);
}
