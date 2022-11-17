package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.CourseDto;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto);

    CourseDto getCourseById(int id);

    List<CourseDto> getAllCourses();

    List<CourseDto> getCoursesByStudentId(int studentId);
}
