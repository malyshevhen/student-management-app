package ua.com.foxstudent102052.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.RepositoryException;
import ua.com.foxstudent102052.repository.StudentRepository;

@Slf4j
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT_WITH_ID_NOT_EXIST = "Student with id %d doesn't exist";
    private final StudentRepository studentRepository;

    @Override
    public void addStudent(StudentDto studentDto) throws ServiceException {
        int studentId = studentDto.getId();

        try {
            var newStudent = StudentMapper.toStudent(studentDto);
            studentRepository.addStudent(newStudent);
            var lastStudentFromDB = studentRepository.getLastStudent();
            var lastStudentFromDBDto = StudentMapper.toDto(lastStudentFromDB);
            if (studentDto.equals(lastStudentFromDBDto)) {
                log.info("Student with id {} was added", studentId);
            } else {
                log.info("Student with id {} wasn't added", studentId);

                throw new ServiceException("Student wasn`t added");
            }

        } catch (RepositoryException e) {
            var msg = String.format("Student with id %d already exist", studentId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public void removeStudent(int studentId) throws ServiceException {

        try {

            if (Boolean.TRUE.equals(ifExist(studentId))) {
                studentRepository.removeStudent(studentId);

                if (Boolean.TRUE.equals(ifExist(studentId))) {
                    log.info("Student with id {} wasn't removed", studentId);

                    throw new ServiceException("Student wasn`t removed");
                } else {
                    log.info("Student with id {} was removed", studentId);
                }
            } else {
                log.info(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));

                throw new ServiceException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
            }

        } catch (RepositoryException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, studentId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public List<StudentDto> getAllStudents() throws ServiceException {

        try {
            return studentRepository.getAllStudents()
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();

        } catch (RepositoryException e) {
            var msg = "There are no students in the database";
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws ServiceException {

        try {
            studentRepository.addStudentToCourse(studentId, courseId);

        } catch (RepositoryException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, studentId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int groupId) throws ServiceException {

        try {
            studentRepository.removeStudentFromCourse(studentId, groupId);

        } catch (RepositoryException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, studentId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }

    }

    @Override
    public List<StudentDto> getStudentsByCourse(int courseId) throws ServiceException {

        try {
            return studentRepository.getStudentsByCourseId(courseId)
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();

        } catch (RepositoryException e) {
            var msg = String.format("There are no students in course with id %d", courseId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public List<StudentDto> getStudentsByNameAndCourse(String studentName, Integer courseId) throws ServiceException {

        try {
            return studentRepository.getStudentsByNameAndCourse(studentName, courseId)
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();

        } catch (RepositoryException e) {
            var msg = String.format("There are no students in course with id %d", courseId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public Boolean ifExist(int id) throws ServiceException {
        try {
            var studentFromDB = studentRepository.getStudentById(id);

            return studentFromDB != null;

        } catch (RepositoryException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, id);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }
}
