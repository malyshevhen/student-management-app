package ua.com.foxstudent102052.repository;

import java.util.List;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

public interface CourseRepository {
    void addCourse(Course course);

    void removeCourse(int id);

    void updateCourseName(int courseId, String courseName);

    void updateCourse(Course course);

    List<Course> getAllCourses();

    Course getCourseById(int courseId);

    Course getCourseByName(String courseName);

    void updateCourseDescription(int courseId, String courseDescription);

    List<Student> getStudentsByCourseId(int course);
}
