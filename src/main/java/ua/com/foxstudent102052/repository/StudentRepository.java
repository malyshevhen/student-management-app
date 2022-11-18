package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface StudentRepository {

    void addStudent(Student student) throws RepositoryException;

    void removeStudent(int id) throws RepositoryException;

    void addStudentToCourse(int studentId, int courseId) throws RepositoryException;

    void removeStudentFromCourse(int studentId, int groupId) throws RepositoryException;

    List<Student> getAllStudents() throws RepositoryException;

    List<Student> getStudentsByCourseId(int course) throws RepositoryException;

    List<Student> getStudentsByNameAndCourse(String studentName, Integer courseId) throws RepositoryException;

    Student getStudentById(int i) throws RepositoryException;
}
