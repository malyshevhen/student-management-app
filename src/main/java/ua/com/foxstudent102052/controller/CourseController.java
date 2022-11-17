package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.StudentService;
import ua.com.foxstudent102052.service.exception.NoSuchCourseExistsException;
import ua.com.foxstudent102052.service.exception.NoSuchStudentExistsException;

import java.util.List;

@Slf4j
public class CourseController {
    private final CourseService courseService;
    
    private final StudentService studentService;
    
    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public List<CourseDto> getAllCourses() {
        try {
            var coursesListDto = courseService.getAllCourses();
            coursesListDto.forEach(courseDto -> courseDto.setStudentsList(getStudentsByCourse(courseDto.getId())));

            return coursesListDto;
        } catch (NoSuchCourseExistsException e) {
            log.info(e.getMessage());
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByCourse(int courseId) {

        try {
            return studentService.getStudentsByCourse(courseId);
            
        } catch (NoSuchCourseExistsException e) {
            log.info(e.getMessage());
        }

        return List.of();
    }
}
