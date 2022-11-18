package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface DAOFactory {
    void setJdbcUrl(String s);

    void setLogin(String postgres);

    void setPassword(String s);

    void doPost(String query) throws DAOException;

    Student getStudent(String query) throws DAOException;

    List<Student> getStudents(String query) throws DAOException;

    Course getCourse(String query) throws DAOException;

    ArrayList<Course> getCourses(String query) throws DAOException;

    Group getGroup(String query) throws DAOException;

    List<Group> getGroups(String query) throws DAOException;

    void executeSqlScript(String string) throws IOException;
}
