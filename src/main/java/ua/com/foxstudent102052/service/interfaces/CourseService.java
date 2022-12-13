package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.dto.CourseDto;
import ua.com.foxstudent102052.service.exceptions.ServiceException;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto) throws ServiceException;

    CourseDto getCourse(int id) throws ServiceException;

    List<CourseDto> getCourses() throws ServiceException;

    List<CourseDto> getCourses(int studentId) throws ServiceException;
}
