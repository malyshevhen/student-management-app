package ua.com.foxstudent102052.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void addCourse(CourseDto courseDto) throws DataAccessException {
        var courseName = courseDto.getCourseName();

        if (courseDao.getCourseByName(courseName).isEmpty()) {
            courseDao.addCourse(modelMapper.map(courseDto, Course.class));
        } else {
            throw new ElementAlreadyExistException(
                    String.format("Course with id %d already exist", courseDto.getCourseId()));
        }
    }

    @Transactional
    @Override
    public CourseDto getCourseById(int courseId) throws DataAccessException {
        var course = courseDao.getCourseById(courseId).orElseThrow();

        return modelMapper.map(course, CourseDto.class);
    }

    @Transactional
    @Override
    public List<CourseDto> getAll() throws DataAccessException {
        if (courseDao.getAll().isEmpty()) {
            throw new NoSuchElementException("There are no courses in database");
        } else {
            return courseDao.getAll()
                    .stream()
                    .map(course -> modelMapper.map(course, CourseDto.class))
                    .toList();
        }
    }

    @Transactional
    @Override
    public List<CourseDto> getCoursesByStudent(int studentId) throws DataAccessException {
        if (courseDao.getCoursesByStudentId(studentId).isEmpty()) {
            throw new NoSuchElementException("There are no students on course");
        } else {
            return courseDao.getCoursesByStudentId(studentId)
                    .stream()
                    .map(course -> modelMapper.map(course, CourseDto.class))
                    .toList();
        }
    }
}
