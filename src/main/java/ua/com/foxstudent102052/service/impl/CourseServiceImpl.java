package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.mapper.CourseModelMapper;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;

    @Override
    public void addCourse(CourseDto course) throws DAOException {
        var courseName = course.getName();

        if (courseDao.getCourse(courseName).isEmpty()) {
            courseDao.addCourse(CourseModelMapper.toCourse(course));
        } else {
            throw new ElementAlreadyExistException(String.format("Course with id %d already exist", course.getId()));
        }
    }

    @Override
    public CourseDto getCourse(int courseId) throws DAOException {
        var course = courseDao.getCourse(courseId).orElseThrow();

        return CourseModelMapper.toCourseDto(course);
    }

    @Override
    public List<CourseDto> getCourses() throws DAOException {
        if (courseDao.getCourses().isEmpty()) {
            throw new NoSuchElementException("There are no courses in database");
        } else {
            return courseDao.getCourses()
                .stream()
                .map(CourseModelMapper::toCourseDto)
                .toList();
        }
    }

    @Override
    public List<CourseDto> getCourses(int studentId) throws DAOException {
        if (courseDao.getCourses(studentId).isEmpty()) {
            throw new NoSuchElementException("There are no students on course");
        } else {
            return courseDao.getCourses(studentId)
                .stream()
                .map(CourseModelMapper::toCourseDto)
                .toList();
        }
    }
}
