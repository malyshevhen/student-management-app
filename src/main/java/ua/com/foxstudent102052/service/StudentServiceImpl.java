package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.StudentRepository;
import ua.com.foxstudent102052.repository.exception.DAOException;
import ua.com.foxstudent102052.service.exception.NoSuchStudentExistsException;
import ua.com.foxstudent102052.service.exception.StudentAlreadyExistException;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT_WITH_ID_NOT_EXIST = "Student with id %d doesn't exist";
    private final StudentRepository studentRepository;

    @Override
    public void addStudent(StudentDto studentDto) {
        int studentId = studentDto.getId();

        try {
            var newStudent = StudentMapper.toStudent(studentDto);
            studentRepository.addStudent(newStudent);

        } catch (DAOException e) {
            var msg = String.format("Student with id %d already exist", studentId);
            log.error(msg, e);

            throw new StudentAlreadyExistException(msg);
        }
    }

    @Override
    public void removeStudent(int studentId) {

        try {
            studentRepository.removeStudent(studentId);

        } catch (DAOException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, studentId);
            log.error(msg, e);

            throw new NoSuchStudentExistsException(msg);
        }
    }

    @Override
    public List<StudentDto> getAllStudents() {

        try {
            return studentRepository.getAllStudents()
                .stream()
                .map(StudentMapper::toDto)
                .toList();

        } catch (DAOException e) {
            var msg = "There are no students in the database";
            log.error(msg, e);

            throw new NoSuchStudentExistsException(msg);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {

        try {
            studentRepository.addStudentToCourse(studentId, courseId);

        } catch (DAOException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, studentId);
            log.error(msg, e);

            throw new NoSuchStudentExistsException(msg);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int groupId) {

        try {
            studentRepository.removeStudentFromCourse(studentId, groupId);

        } catch (DAOException e) {
            var msg = String.format(STUDENT_WITH_ID_NOT_EXIST, studentId);
            log.error(msg, e);

            throw new NoSuchStudentExistsException(msg);
        }

    }

    @Override
    public List<StudentDto> getStudentsByCourse(int courseId) {

        try {
            return studentRepository.getStudentsByCourseId(courseId)
                .stream()
                .map(StudentMapper::toDto)
                .toList();

        } catch (DAOException e) {
            var msg = String.format("There are no students in course with id %d", courseId);
            log.error(msg, e);
            throw new NoSuchStudentExistsException(msg);
        }
    }

    @Override
    public List<StudentDto> getStudentsByCourseNameAndGroupId(String studentName, Integer courseId) {

        try {
            return studentRepository.getStudentsByCourseNameAndGroupId(studentName, courseId)
                .stream()
                .map(StudentMapper::toDto)
                .toList();

        } catch (DAOException e) {
            var msg = String.format("There are no students in course with id %d", courseId);
            log.error(msg, e);
            throw new NoSuchStudentExistsException(msg);
        }
    }
}
