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
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT_DOES_NOT_EXIST = "Student with id %d doesn't exist";
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final ModelMapper modelMapper;

    @Override
    public void addStudent(StudentDto studentDto) throws DataAccessException {
        var student = modelMapper.map(studentDto, Student.class);
        studentDao.save(student);
    }

    @Override
    public void removeStudent(int studentId) throws DataAccessException {
        var student = studentDao.findById(studentId).orElseThrow(
                () -> new NoSuchElementException(String.format(STUDENT_DOES_NOT_EXIST, studentId)));

        studentDao.delete(student);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws DataAccessException {
        var student = studentDao.findById(studentId).orElseThrow(
                () -> new NoSuchElementException(String.format(STUDENT_DOES_NOT_EXIST, studentId)));
        var course = courseDao.findById(courseId).orElseThrow(
                () -> new NoSuchElementException(String.format("Course with id %d doesn't exist", courseId)));

        student.addCourse(course);

        studentDao.save(student);
        courseDao.save(course);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws DataAccessException {
        var student = studentDao.findById(studentId).orElseThrow(
                () -> new NoSuchElementException(String.format(STUDENT_DOES_NOT_EXIST, studentId)));
        var course = courseDao.findById(courseId).orElseThrow(
                () -> new NoSuchElementException(String.format("Course with id %d doesn't exist", courseId)));

        boolean attended = student.getCourses()
                .stream()
                .anyMatch(c -> c.equals(course));

        if (attended) {
            student.removeCourse(course);

            studentDao.save(student);
            courseDao.save(course);
        } else {
            throw new NoSuchElementException("This student not attend this course");
        }
    }

    @Override
    public List<StudentDto> getAll() throws DataAccessException {
        return studentDao.findAll()
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();
    }

    @Override
    public List<StudentDto> getStudentsByCourse(int courseId) {
        var studentDtoList = studentDao.findByCourseId(courseId)
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();

        if (studentDtoList.isEmpty()) {
            throw new NoSuchElementException("There are no students on course");
        } else {
            return studentDtoList;
        }
    }

    @Override
    public List<StudentDto> getStudentsByGroup(int groupId) throws DataAccessException {
        var studentDtoList = studentDao.findByGroupId(groupId)
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();

        if (studentDtoList.isEmpty()) {
            throw new NoSuchElementException("There are no students on course");
        } else {
            return studentDtoList;
        }
    }

    @Override
    public List<StudentDto> getStudentsByNameAndCourse(String studentName, Integer courseId)
            throws DataAccessException {
        var studentDtoList = studentDao.findByNameAndCourseId(studentName, courseId)
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();

        if (studentDtoList.isEmpty()) {
            throw new NoSuchElementException(
                    String.format("There are no students with name '%s' present on '%d' course", studentName,
                            courseId));
        } else {
            return studentDtoList;
        }
    }
}
