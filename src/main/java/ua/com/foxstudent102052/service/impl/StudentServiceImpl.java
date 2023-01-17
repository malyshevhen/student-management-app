package ua.com.foxstudent102052.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public void addStudent(StudentDto studentDto) throws DataAccessException {
        var student = modelMapper.map(studentDto, Student.class);
        studentDao.addStudent(student);
    }

    @Override
    public void removeStudent(int studentId) throws DataAccessException {
        if (studentDao.getStudent(studentId).isPresent()) {
            studentDao.removeStudent(studentId);
        } else {
            throw new NoSuchElementException(String.format("Student with id %d doesn't exist", studentId));
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws DataAccessException {
        if (studentDao.getStudent(studentId).isPresent()) {
            studentDao.addStudentToCourse(studentId, courseId);
        } else {
            throw new NoSuchElementException(String.format("Student with id %d doesn't exist", studentId));
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws DataAccessException {
        boolean studentPresentInCourse = studentDao.getStudentsByCourse(courseId)
                .stream()
                .anyMatch(student -> student.getId() == studentId);

        if (studentPresentInCourse) {
            studentDao.removeStudentFromCourse(studentId, courseId);
        } else {
            throw new NoSuchElementException("This student not attend this course");
        }
    }

    @Override
    public List<StudentDto> getAll() throws DataAccessException {
        var studentDtoList = studentDao.getAll()
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();

        if (studentDtoList.isEmpty()) {
            throw new NoSuchElementException("There are no students in DB");
        } else {
            return studentDtoList;
        }
    }

    @Override
    public List<StudentDto> getStudentsByCourse(int courseId) throws DataAccessException {
        var studentDtoList = studentDao.getStudentsByCourse(courseId)
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
        var studentDtoList = studentDao.getStudentsByGroup(groupId)
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
        var studentDtoList = studentDao.getStudentsByNameAndCourse(studentName, courseId)
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
