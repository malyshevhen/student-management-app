package ua.com.foxstudent102052.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseController {
    private final CourseService courseService;

    public List<CourseDto> getAllCourses() throws DataAccessException {
        return courseService.getAll();
    }
}
