package ua.com.foxstudent102052.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.CourseRepository;
import ua.com.foxstudent102052.dao.interfaces.StudentRepository;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT_DOES_NOT_EXIST = "Student with id %d doesn't exist";
    private final StudentRepository studentDao;
    private final CourseRepository courseDao;
    private final ModelMapper modelMapper;

    @Override
    public StudentDto addStudent(StudentDto studentDto) throws DataAccessException {
        var student = modelMapper.map(studentDto, Student.class);
        var savedStudent = studentDao.save(student);

        return modelMapper.map(savedStudent, StudentDto.class);
    }

    @Override
    public void removeStudent(Long studentId) throws DataAccessException {
        var student = studentDao.findById(studentId)
            .orElseThrow(
                () -> new NoSuchElementException(String.format(STUDENT_DOES_NOT_EXIST, studentId))
            );

        studentDao.delete(student);
    }

    @Override
    public void addStudentToCourse(Long studentId, Long courseId) throws DataAccessException {
        var student = studentDao.findById(studentId)
            .orElseThrow(
                () -> new NoSuchElementException(String.format(STUDENT_DOES_NOT_EXIST, studentId))
            );
        var course = courseDao.findById(courseId)
            .orElseThrow(
                () -> new NoSuchElementException(String.format("Course with id %d doesn't exist", courseId))
            );

        student.addCourse(course);
    }

    @Override
    public void removeStudentFromCourse(Long studentId, Long courseId) throws DataAccessException {
        var student = studentDao.findById(studentId)
            .orElseThrow(
                () -> new NoSuchElementException(String.format(STUDENT_DOES_NOT_EXIST, studentId))
            );
        var course = courseDao.findById(courseId)
            .orElseThrow(
                () -> new NoSuchElementException(String.format("Course with id %d doesn't exist", courseId))
            );

        boolean attended = student.getCourses()
            .stream()
            .anyMatch(c -> c.equals(course));

        if (attended) {
            student.removeCourse(course);
        } else {
            throw new NoSuchElementException("This student not attend this course");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudentDto> getAll() throws DataAccessException {
        return studentDao.findAll()
            .stream()
            .map(student -> modelMapper.map(student, StudentDto.class))
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudentDto> getStudentsByCourse(Long courseId) {
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

    @Transactional(readOnly = true)
    @Override
    public List<StudentDto> getStudentsByGroup(Long groupId) throws DataAccessException {
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

    @Transactional(readOnly = true)
    @Override
    public List<StudentDto> getStudentsByNameAndCourse(String studentName, Long courseId) throws DataAccessException {
        var studentDtoList = studentDao.findByNameAndCourseId(studentName, courseId)
            .stream()
            .map(student -> modelMapper.map(student, StudentDto.class))
            .toList();

        if (studentDtoList.isEmpty()) {
            throw new NoSuchElementException(
                String.format(
                    "There are no students with name '%s' present on '%d' course",
                    studentName,
                    courseId
                )
            );
        } else {
            return studentDtoList;
        }
    }

    @Override
    public Student getStudent(Long id) {
        return studentDao.findById(id).orElseThrow(
            () -> new NoSuchElementException("Student not found")
        );
    }
}
