package ua.com.foxstudent102052.service.interfaces;

import java.util.List;

import ua.com.foxstudent102052.model.dto.CourseDto;

public interface CourseService {
    void addCourse(CourseDto courseDto);

    CourseDto getCourse(int id);

    List<CourseDto> getCourses();

    List<CourseDto> getCourses(int studentId);
}
