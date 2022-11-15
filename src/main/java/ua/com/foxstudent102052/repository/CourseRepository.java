package ua.com.foxstudent102052.repository;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface CourseRepository {
    void addCourse(Course course);

    void removeCourse(int id);

    void updateCourse(Course course);

    List<Course> getAllCourses();

    Course getCourseById(int courseId);

    Course getCourseByName(String courseName);

    List<Student> getStudentsByCourseId(int course);
}
