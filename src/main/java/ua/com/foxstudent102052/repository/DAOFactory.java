package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.util.ArrayList;
import java.util.List;

public interface DAOFactory {
    void setJdbcUrl(String s);

    void setLogin(String postgres);

    void setPassword(String s);

    void doPost(String query);

    Student getStudent(String query);

    List<Student> getStudents(String query);

    Course getCourse(String query);

    ArrayList<Course> getCourses(String query);

    Group getGroup(String query);

    List<Group> getGroups(String query);
}
