package ua.com.foxstudent102052.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
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

    @Override
    public void addCourse(CourseDto courseDto) throws DAOException {
        var courseName = courseDto.getName();

        if (courseDao.getCourse(courseName).isEmpty()) {
            courseDao.addCourse(modelMapper.map(courseDto, Course.class));
        } else {
            throw new ElementAlreadyExistException(String.format("Course with id %d already exist", courseDto.getId()));
        }
    }

    @Override
    public CourseDto getCourse(int courseId) throws DAOException {
        var course = courseDao.getCourse(courseId).orElseThrow();

        return modelMapper.map(course, CourseDto.class);
    }

    @Override
    public List<CourseDto> getCourses() throws DAOException {
        if (courseDao.getCourses().isEmpty()) {
            throw new NoSuchElementException("There are no courses in database");
        } else {
            return courseDao.getCourses()
                    .stream()
                    .map(course -> modelMapper.map(course, CourseDto.class))
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
                    .map(course -> modelMapper.map(course, CourseDto.class))
                    .toList();
        }
    }
}
