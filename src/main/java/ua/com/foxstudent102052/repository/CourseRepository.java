package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Course;

import java.util.List;

public interface CourseRepository {
    void addCourse(Course course) throws RepositoryException;

    List<Course> getAllCourses() throws RepositoryException;

    Course getCourseById(int courseId) throws RepositoryException;

    List<Course> getCoursesByStudentId(int studentId) throws RepositoryException;

    Course getLastCourse() throws RepositoryException;
}
