package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    void addCourse(Course course) throws DAOException;

    List<Course> getCourses() throws DAOException;

    Optional<Course> getCourse(int courseId) throws DAOException;
    Optional<Course> getCourse(String courseName) throws DAOException;

    List<Course> getCourses(int studentId) throws DAOException;
}
