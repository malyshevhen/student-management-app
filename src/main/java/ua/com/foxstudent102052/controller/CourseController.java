package ua.com.foxstudent102052.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final StudentService studentService;

    public List<CourseDto> getAllCourses() throws DAOException {
        return courseService.getCourses().stream()
            .map(this::setStudentsToCourse)
            .toList();
    }

    private CourseDto setStudentsToCourse(CourseDto courseDto) {
        try {
            var students = studentService.getStudents(courseDto.getId());
            courseDto.setStudentsList(students);

            return courseDto;
        } catch (NoSuchElementException | DAOException e) {
            return courseDto;
        }
    }
}
