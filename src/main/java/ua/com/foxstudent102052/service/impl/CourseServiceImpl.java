package ua.com.foxstudent102052.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao;
    private static final ModelMapper modelMapper = new ModelMapper();

    @Override
    public void addCourse(CourseDto courseDto) throws DAOException {
        var courseName = courseDto.getName();

        if (courseDao.getCourseByName(courseName).isEmpty()) {
            courseDao.addCourse(modelMapper.map(courseDto, Course.class));
        } else {
            throw new ElementAlreadyExistException(String.format("Course with id %d already exist", courseDto.getId()));
        }
    }

    @Override
    public CourseDto getCourseById(int courseId) throws DAOException {
        var course = courseDao.getCourseById(courseId).orElseThrow();

        return modelMapper.map(course, CourseDto.class);
    }

    @Override
    public List<CourseDto> getAll() throws DAOException {
        if (courseDao.getAll().isEmpty()) {
            throw new NoSuchElementException("There are no courses in database");
        } else {
            return courseDao.getAll()
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
        }
    }

    @Override
    public List<CourseDto> getCoursesByStudent(int studentId) throws DAOException {
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
