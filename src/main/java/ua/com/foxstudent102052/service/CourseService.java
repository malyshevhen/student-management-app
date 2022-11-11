package ua.com.foxstudent102052.service;

import java.util.List;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

public interface CourseService {
    void addCourse(Course course);

    void removeCourse(int id);

    void updateCourseName(int courseId, String courseName);

    void updateCourse(Course course);

    Course getCourseById(int id);

    List<Course> getAllCourses();

    Course getCourseByName(String name);

    List<Student> getStudentsByCourse(int courseId);

    void updateCourseDescription(int courseId, String courseDescription);
}
