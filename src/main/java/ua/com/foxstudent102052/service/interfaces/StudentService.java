package ua.com.foxstudent102052.service.interfaces;

import ua.com.foxstudent102052.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ServiceException;

import java.util.List;

public interface StudentService {
    Boolean ifExist(int id) throws ServiceException;

    void addStudent(StudentDto studentDto) throws ServiceException;

    void addStudentToCourse(int studentId, int courseId) throws ServiceException;

    void removeStudent(int id) throws ServiceException;

    void removeStudentFromCourse(int studentId, int groupId) throws ServiceException;

    List<StudentDto> getStudents() throws ServiceException;

    List<StudentDto> getStudents(int courseId) throws ServiceException;

    List<StudentDto> getStudents(String studentName, Integer courseId) throws ServiceException;
}
