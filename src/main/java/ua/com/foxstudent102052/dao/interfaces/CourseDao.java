package ua.com.foxstudent102052.dao.interfaces;

import ua.com.foxstudent102052.model.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    void addCourse(Course course);

    List<Course> getAll();

    Optional<Course> getCourseById(int courseId);

    Optional<Course> getCourseByName(String courseName);

    List<Course> getCoursesByStudentId(int studentId);
}
