package ua.com.foxstudent102052.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxstudent102052.dao.interfaces.CourseRepository;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.CourseService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseDao;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = false)
    @Override
    public void addCourse(CourseDto courseDto) throws DataAccessException {
        var courseName = courseDto.getCourseName();

        if (courseDao.findByCourseName(courseName).isEmpty()) {
            courseDao.save(modelMapper.map(courseDto, Course.class));
        } else {
            throw new ElementAlreadyExistException(
                    String.format("Course with id %d already exist", courseDto.getCourseId()));
        }
    }

    @Override
    public CourseDto getCourseById(int courseId) throws DataAccessException {
        var course = courseDao.findById(courseId).orElseThrow();

        return modelMapper.map(course, CourseDto.class);
    }

    @Override
    public List<CourseDto> getAll() throws DataAccessException {
        if (courseDao.findAll().isEmpty()) {
            throw new NoSuchElementException("There are no courses in database");
        } else {
            return courseDao.findAll()
                    .stream()
                    .map(course -> modelMapper.map(course, CourseDto.class))
                    .toList();
        }
    }

    @Override
    public List<CourseDto> getCoursesByStudent(int studentId) throws DataAccessException {
        if (courseDao.findByStudentId(studentId).isEmpty()) {
            throw new NoSuchElementException("There are no students on course");
        } else {
            return courseDao.findByStudentId(studentId)
                    .stream()
                    .map(course -> modelMapper.map(course, CourseDto.class))
                    .toList();
        }
    }
}
