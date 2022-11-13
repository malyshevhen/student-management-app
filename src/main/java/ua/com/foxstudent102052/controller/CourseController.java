package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;

import java.util.List;

@Slf4j
public class CourseController {
    private static CourseService courseService;
    
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    public void addCourse(String courseName, String courseDescription) {
        try {
            courseService.addCourse(new Course(courseName, courseDescription));
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public void updateCourse(CourseDto courseDto) {
        try {
            courseService.updateCourse(CourseMapper.dtoToCourse(courseDto));
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public void updateCourseName(int courseId, String courseName) {
        try {
            courseService.updateCourseName(courseId, courseName);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public void updateCourseDescription(int courseId, String courseDescription) {
        try {
            courseService.updateCourseDescription(courseId, courseDescription);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public void removeCourse(int courseId) {
        try {
            courseService.removeCourse(courseId);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public CourseDto getCourseById(int courseId) {
        try {
            CourseDto courseDto = CourseMapper.courseToDto(courseService.getCourseById(courseId));
            courseDto.setStudentsList(getStudentsByCourse(courseId));

            return courseDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return new CourseDto();
    }

    public List<CourseDto> getAllCourses() {
        try {
            var coursesListDto = courseService.getAllCourses()
                .stream()
                .map(CourseMapper::courseToDto)
                .toList();
            coursesListDto.forEach(courseDto -> courseDto.setStudentsList(getStudentsByCourse(courseDto.getId())));

            return coursesListDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByCourse(int courseId) {

        try {
            return courseService.getStudentsByCourse(courseId)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return List.of();
    }

    public CourseDto getCourseByName(String courseName) {
        try {
            CourseDto courseDto = CourseMapper.courseToDto(courseService.getCourseByName(courseName));
            courseDto.setStudentsList(getStudentsByCourse(courseDto.getId()));

            return courseDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return new CourseDto();
    }
}
