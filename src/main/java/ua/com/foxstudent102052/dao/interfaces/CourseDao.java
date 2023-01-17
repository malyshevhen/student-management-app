package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;
import java.util.Optional;

import ua.com.foxstudent102052.model.entity.Course;

public interface CourseDao {
    void addCourse(Course course);

    List<Course> getCourses();

    Optional<Course> getCourse(int courseId);

    Optional<Course> getCourse(String courseName);

    List<Course> getCourses(int studentId);
}
