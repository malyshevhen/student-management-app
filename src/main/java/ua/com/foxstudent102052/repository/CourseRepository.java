package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Course;

import java.util.List;

public interface CourseRepository {
    void addCourse(Course course);

    List<Course> getAllCourses();

    Course getCourseById(int courseId);

    List<Course> getCoursesByStudentId(int studentId);
}
