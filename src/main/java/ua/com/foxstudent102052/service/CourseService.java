package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.CourseDto;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto) throws ServiceException;

    CourseDto getCourseById(int id) throws ServiceException;

    List<CourseDto> getAllCourses() throws ServiceException;

    List<CourseDto> getCoursesByStudentId(int studentId) throws ServiceException;
}
