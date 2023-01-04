package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.mapper.StudentModelMapper;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;

    @Override
    public void addStudent(StudentDto studentDto) throws ServiceException {
        try {
            var student = StudentModelMapper.toStudent(studentDto);
            studentDao.addStudent(student);
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public void removeStudent(int studentId) throws ServiceException {
        try {
            if (studentDao.getStudent(studentId).isPresent()) {
                studentDao.removeStudent(studentId);
            } else {
                throw new NoSuchElementException(String.format("Student with id %d doesn't exist", studentId));
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws ServiceException {
        try {
            if (studentDao.getStudent(studentId).isPresent()) {
                studentDao.addStudentToCourse(studentId, courseId);
            } else {
                throw new NoSuchElementException(String.format("Student with id %d doesn't exist", studentId));
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws ServiceException {
        try {
            boolean studentPresentInCourse = studentDao.getStudents(courseId)
                .stream()
                .anyMatch(student -> student.studentId() == studentId);

            if (studentPresentInCourse) {
                studentDao.removeStudentFromCourse(studentId, courseId);
            } else {
                throw new NoSuchElementException("This student not attend this course");
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<StudentDto> getStudents() throws ServiceException {
        try {
            var studentDtoList = studentDao.getStudents()
                .stream()
                .map(StudentModelMapper::toStudentDto)
                .toList();

            if (studentDtoList.isEmpty()) {
                throw new NoSuchElementException("There are no students in DB");
            } else {
                return studentDtoList;
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<StudentDto> getStudents(int courseId) throws ServiceException {
        try {
            var studentDtoList = studentDao.getStudents(courseId)
                .stream()
                .map(StudentModelMapper::toStudentDto)
                .toList();

            if (studentDtoList.isEmpty()) {
                throw new NoSuchElementException("There are no students on course");
            } else {
                return studentDtoList;
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<StudentDto> getStudents(String studentName, Integer courseId) throws ServiceException {
        try {
            var studentDtoList = studentDao.getStudents(studentName, courseId)
                .stream()
                .map(StudentModelMapper::toStudentDto)
                .toList();

            if (studentDtoList.isEmpty()) {
                throw new NoSuchElementException(
                    String.format("There are no students with name '%s' present on '%d' course", studentName, courseId));
            } else {
                return studentDtoList;
            }
        } catch (DAOException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }
}
