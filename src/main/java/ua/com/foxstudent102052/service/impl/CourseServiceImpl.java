package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.mapper.CourseModelMapper;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;

    @Override
    public void addCourse(CourseDto course) throws ServiceException {
        try {
            var courseName = course.getName();

            if (courseDao.getCourse(courseName).isEmpty()) {
                courseDao.addCourse(CourseModelMapper.toCourse(course));
            } else {
                var message = String.format("Course with id %d already exist", course.getId());

                log.error(message);

                throw new ServiceException(message);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public CourseDto getCourse(int courseId) throws ServiceException {
        try {
            var course = courseDao.getCourse(courseId).orElseThrow();

            return CourseModelMapper.toCourseDto(course);
        } catch (DAOException e) {
            log.error(e.getMessage());

            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<CourseDto> getCourses() throws ServiceException {
        try {
            if (courseDao.getCourses().isEmpty()) {
                throw new NoSuchElementException("There are no courses in database");
            } else {
                return courseDao.getCourses()
                    .stream()
                    .map(CourseModelMapper::toCourseDto)
                    .toList();
            }
        } catch (DAOException e) {
            log.error(e.getMessage());

            throw new ServiceException(e);
        }
    }

    @Override
    public List<CourseDto> getCourses(int studentId) throws ServiceException {
        try {
            if (courseDao.getCourses(studentId).isEmpty()) {
                throw new ServiceException("There are no students on course");
            } else {
                return courseDao.getCourses(studentId)
                    .stream()
                    .map(CourseModelMapper::toCourseDto)
                    .toList();
            }
        } catch (DAOException e) {
            log.error(e.getMessage());

            throw new ServiceException(e);
        }
    }
}
