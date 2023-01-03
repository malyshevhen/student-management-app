package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.controller.exceptions.ControllerException;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.StudentService;

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
            var coursesListDto = courseService.getCourses();
            log.info("All courses were successfully received");

            coursesListDto.forEach(courseDto -> {

                try {
                    var studentsByCourse = getStudents(courseDto.getId());
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

    public List<StudentDto> getStudents(int courseId) throws ControllerException {

        try {
            var studentsByCourse = studentService.getStudents(courseId);
            log.info("Students were successfully received");
            return studentsByCourse;

        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new ControllerException(e.getMessage(), e);
        }
    }
}
