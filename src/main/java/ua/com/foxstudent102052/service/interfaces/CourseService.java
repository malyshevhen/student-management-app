package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.model.dto.CourseDto;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto);

    CourseDto getCourseById(int id);

    List<CourseDto> getAll();

    List<CourseDto> getCoursesByStudent(int studentId);
}
