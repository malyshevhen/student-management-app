package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface StudentRepository {

    void addStudent(Student student);

    void removeStudent(int id);

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
