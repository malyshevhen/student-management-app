package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface StudentRepository {

    void addStudent(Student student);

    void removeStudent(int id);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int groupId);

    List<Student> getAllStudents();

    List<Student> getStudentsByCourseId(int course);

    List<Student> getStudentsByCourseNameAndGroupId(String studentName, Integer courseId);
}
