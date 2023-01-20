package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;
import java.util.Optional;

import ua.com.foxstudent102052.model.entity.Course;

public interface CourseDao {
    void addCourse(Course course);

    List<Course> getAll();

    Optional<Course> getCourseById(int courseId);

    Optional<Course> getCourseByName(String courseName);

    List<Course> getCoursesByStudentId(int studentId);
}
