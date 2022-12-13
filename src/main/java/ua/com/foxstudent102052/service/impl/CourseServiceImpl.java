package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.dto.CourseDto;
import ua.com.foxstudent102052.repository.interfaces.CourseRepository;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final String COURSE_ID_DOES_NOT_EXIST = "Course with id %d doesn't exist";
    private final CourseRepository courseRepository;

    @Override
    public void addCourse(CourseDto course) throws ServiceException {
        var newCourse = CourseMapper.toCourse(course);

        try {
            courseRepository.addCourse(newCourse);
        } catch (RepositoryException e) {
            log.error("Course with id {} already exist", course.getId());

            throw new ServiceException(String.format("Course with id %d already exist", course.getId()), e);
        }
    }

    @Override
    public CourseDto getCourse(int courseId) throws ServiceException {

        try {
            var courseDto = CourseMapper.toCourseDto(courseRepository.getCourseById(courseId));

            log.info("Course with id {} was received", courseId);

            return courseDto;
        } catch (RepositoryException e) {
            log.error("Course with id {} doesn't exist", courseId);

            throw new ServiceException(String.format(COURSE_ID_DOES_NOT_EXIST, courseId), e);
        }
    }

    @Override
    public List<CourseDto> getCourses() throws ServiceException {
        try {
            var courseDtoList = courseRepository.getAllCourses()
                .stream()
                .map(CourseMapper::toCourseDto)
                .toList();

            log.info("All courses were received");

            return courseDtoList;
        } catch (RepositoryException e) {
            log.error("There are no courses in database");

            throw new ServiceException("There are no courses in database", e);
        }
    }

    @Override
    public List<CourseDto> getCourses(int studentId) throws ServiceException {
        try {
            var courseDtos = courseRepository.getCoursesByStudentId(studentId)
                .stream()
                .map(CourseMapper::toCourseDto)
                .toList();

            log.info("Courses with student id {} were received", studentId);

            return courseDtos;
        } catch (RepositoryException e) {
            log.error("Student with id {} doesn't have any courses", studentId);

            throw new ServiceException(String.format("Student with id %d doesn't have any courses", studentId), e);
        }
    }
}
