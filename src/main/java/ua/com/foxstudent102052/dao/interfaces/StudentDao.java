package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    void addStudent(Student student) throws DAOException;

    void removeStudent(int id) throws DAOException;

    void addStudentToCourse(int studentId, int courseId) throws DAOException;

    void removeStudentFromCourse(int studentId, int groupId) throws DAOException;

    List<Student> getStudents() throws DAOException;

    List<Student> getStudents(int course) throws DAOException;

    List<Student> getStudents(String studentName, Integer courseId) throws DAOException;

    Optional<Student> getStudent(int i) throws DAOException;
}
