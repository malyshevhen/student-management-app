package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.model.dto.CourseDto;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto);

    CourseDto getCourse(int id);

    List<CourseDto> getCourses();

    List<CourseDto> getCourses(int studentId);
}
