package ua.com.foxstudent102052.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.ServiceException;
import ua.com.foxstudent102052.service.StudentService;

@Slf4j
public class CourseController {
    private final CourseService courseService;

    private final StudentService studentService;

    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public List<CourseDto> getAllCourses() throws ControllerException {
        try {
            var coursesListDto = courseService.getAllCourses();
            log.info("All courses were successfully received");

            coursesListDto.forEach(courseDto -> {

                try {
                    courseDto.setStudentsList(getStudentsByCourse(courseDto.getId()));
                    log.info("All students were successfully received");
                } catch (ControllerException e) {
                    log.error("Students were not received", e);

                    throw new IllegalArgumentException(e);
                }
            });

            return coursesListDto;
        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new ControllerException(e.getMessage(), e);
        }
    }

    public List<StudentDto> getStudentsByCourse(int courseId) throws ControllerException {

        try {
            return studentService.getStudentsByCourse(courseId);

        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new ControllerException(e.getMessage(), e);
        }
    }
}
