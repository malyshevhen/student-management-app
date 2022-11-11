package ua.com.foxstudent102052.repository;

import java.util.List;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

public interface StudentRepository {

    void addStudent(Student student);

    void removeStudent(int id);

    void updateStudentFirstName(int studentId, String firstName);

    void updateStudentLastName(int studentId, String lastName);

    void updateStudentGroup(int studentId, int groupId);

    void updateStudent(Student student);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int groupId);

    Student getStudentById(int id);

    List<Student> getAllStudents();

    List<Student> getStudentsByFirstName(String firstName);

    List<Student> getStudentsByLastName(String lastName);

    List<Student> getStudentsByFullName(String firstName, String lastName);

    List<Course> getCoursesByStudentId(int studentId);
}
