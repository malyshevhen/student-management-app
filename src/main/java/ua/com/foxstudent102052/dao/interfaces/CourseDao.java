package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.model.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    void addCourse(Course course);

    List<Course> getCourses();

    Optional<Course> getCourse(int courseId);

    Optional<Course> getCourse(String courseName);

    List<Course> getCourses(int studentId);
}
