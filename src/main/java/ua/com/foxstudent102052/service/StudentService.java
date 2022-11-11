package ua.com.foxstudent102052.service;

import java.util.List;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

public interface StudentService {


    void addStudent(Student student);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudent(int id);

    void removeStudentFromCourse(int studentId, int groupId);

    void updateStudentFirstName(int studentId, String firstName);

    void updateStudentLastName(int studentId, String lastName);

    void updateStudentGroup(int studentId, int groupId);

    void updateStudent(Student student);

    Student getStudentById(int id);

    List<Student> getAllStudents();

    List<Student> getStudentsByLastName(String lastName);

    List<Student> getStudentsByName(String name);

    List<Student> getStudentsBySurnameAndName(String firstName, String lastName);

    List<Course> getCoursesByStudentId(int studentId);
}
