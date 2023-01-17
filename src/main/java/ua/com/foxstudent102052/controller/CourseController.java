package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseController {
    private final CourseService courseService;
    private final StudentService studentService;

    public List<CourseDto> getAllCourses() throws DAOException {
        return courseService.getAll().stream()
                .map(this::setStudentsToCourse)
                .toList();
    }

    private CourseDto setStudentsToCourse(CourseDto courseDto) {
        try {
            var students = studentService.getStudentsByCourse(courseDto.getId());
            courseDto.setStudentList(students);

            return courseDto;
        } catch (NoSuchElementException | DAOException e) {
            return courseDto;
        }
    }
}
