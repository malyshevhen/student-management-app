package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.ServiceException;
import ua.com.foxstudent102052.service.StudentService;

import java.util.List;

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
                    var studentsByCourse = getStudentsByCourse(courseDto.getId());
                    courseDto.setStudentsList(studentsByCourse);
                    log.info("All students were successfully received");

                } catch (ControllerException e) {
                    log.info("Students were not received", e);
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
            var studentsByCourse = studentService.getStudentsByCourse(courseId);
            log.info("Students were successfully received");
            return studentsByCourse;

        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new ControllerException(e.getMessage(), e);
        }
    }
}
