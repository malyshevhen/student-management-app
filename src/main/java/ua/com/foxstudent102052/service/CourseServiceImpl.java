package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.repository.CourseRepository;
import ua.com.foxstudent102052.repository.RepositoryException;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final String COURSE_ID_DOES_NOT_EXIST = "Course with id %d doesn't exist";
    private final CourseRepository courseRepository;

    @Override
    public void addCourse(CourseDto course) throws ServiceException {

        try {
            courseRepository.addCourse(CourseMapper.toCourse(course));

        } catch (RepositoryException e) {
            var msg = String.format("Course with %s already exists", course.getName());
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public List<CourseDto> getAllCourses() throws ServiceException {

        try {
            return courseRepository.getAllCourses()
                .stream()
                .map(CourseMapper::toDto)
                .toList();
        } catch (RepositoryException e) {
            var msg = "There are no courses in database";
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public CourseDto getCourseById(int id) throws ServiceException {

        try {
            return CourseMapper.toDto(courseRepository.getCourseById(id));

        } catch (RepositoryException e) {
            var msg = String.format(COURSE_ID_DOES_NOT_EXIST, id);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public List<CourseDto> getCoursesByStudentId(int studentId) throws ServiceException {

        try {
            return courseRepository.getCoursesByStudentId(studentId)
                .stream()
                .map(CourseMapper::toDto)
                .toList();

        } catch (RepositoryException e) {
            String msg = String.format("Student with id %d doesn't have any courses", studentId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }
}
