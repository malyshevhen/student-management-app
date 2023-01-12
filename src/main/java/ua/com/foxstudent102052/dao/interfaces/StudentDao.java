package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    void addStudent(Student student);

    void removeStudent(int id);

    void addStudentToCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int groupId);

    List<Student> getStudents();

    List<Student> getStudentsByCourse(int courseId);

    List<Student> getStudentsByGroup(int groupId);

    List<Student> getStudents(String studentName, int courseId);

    Optional<Student> getStudent(int id);
}
