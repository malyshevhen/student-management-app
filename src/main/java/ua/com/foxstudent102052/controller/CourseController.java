package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class CourseController {
    private final CourseService courseService;
    
    private final StudentService studentService;
    
    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public List<CourseDto> getAllCourses() throws NoSuchElementException {
        try {
            var coursesListDto = courseService.getAllCourses();
            coursesListDto.forEach(courseDto -> courseDto.setStudentsList(getStudentsByCourse(courseDto.getId())));

            return coursesListDto;
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            
            throw new NoSuchElementException(e.getMessage(), e);
        }
    }

    public List<StudentDto> getStudentsByCourse(int courseId) throws NoSuchElementException {

        try {
            return studentService.getStudentsByCourse(courseId);
            
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());

            throw new NoSuchElementException(e.getMessage(), e);
        }
    }
}
