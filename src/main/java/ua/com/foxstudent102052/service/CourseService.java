package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto);

    void removeCourse(int id);

    void updateCourseName(CourseDto courseDto);

    void updateCourseDescription(CourseDto courseDto);

    void updateCourse(CourseDto courseDto);

    Course getCourseById(int id);

    List<Course> getAllCourses();

    Course getCourseByName(String name);

    List<Student> getStudentsByCourse(int courseId);
}
