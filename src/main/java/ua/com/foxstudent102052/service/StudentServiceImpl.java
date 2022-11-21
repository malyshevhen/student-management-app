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

        try {
            var newStudent = StudentMapper.toStudent(studentDto);
            studentRepository.addStudent(newStudent);
            var lastStudentFromDB = studentRepository.getLastStudent();

            if (newStudent.equals(lastStudentFromDB)) {
                log.info("Student with id {} was added", lastStudentFromDB.getStudentId());

            } else {
                log.info("Student with id {} wasn't added", lastStudentFromDB.getStudentId());

                throw new ServiceException("Student wasn`t added");
            }

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
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
                log.error(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));

                throw new ServiceException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
            }

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<StudentDto> getAllStudents() throws ServiceException {

        try {
            var allStudentsList = studentRepository.getAllStudents()
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();
            log.info("All students were received");

            return allStudentsList;

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws ServiceException {

        try {
            studentRepository.addStudentToCourse(studentId, courseId);
            log.info("Student with id {} was added to course with id {}", studentId, courseId);

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);

        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int groupId) throws ServiceException {

        try {
            studentRepository.removeStudentFromCourse(studentId, groupId);
            log.info("Student with id {} was removed from course with id {}", studentId, groupId);

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }

    }

    @Override
    public List<StudentDto> getStudentsByCourse(int courseId) throws ServiceException {

        try {
            var studentsByCourseList = studentRepository.getStudentsByCourseId(courseId)
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();

            log.info("All students from course with id {} were received", courseId);

            return studentsByCourseList;

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<StudentDto> getStudentsByNameAndCourse(String studentName, Integer courseId) throws ServiceException {

        try {
            var studentsByNameAndCourseList = studentRepository.getStudentsByNameAndCourse(studentName, courseId)
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();

            log.info("All students with name {} from course with id {} were received", studentName, courseId);

            return studentsByNameAndCourseList;

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public Boolean ifExist(int id) {
        try {
            studentRepository.getStudentById(id);
            log.info("Student with id {} exist", id);

            return true;

        } catch (RepositoryException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, id);
            log.error(msg, e);

            return false;
        }
    }
}
