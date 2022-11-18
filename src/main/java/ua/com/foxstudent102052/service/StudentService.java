package ua.com.foxstudent102052.service;

import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

public interface StudentService {
    Boolean ifExist(int id) throws ServiceException;

    void addStudent(StudentDto studentDto) throws ServiceException;

    void addStudentToCourse(int studentId, int courseId) throws ServiceException;

    void removeStudent(int id) throws ServiceException;

    void removeStudentFromCourse(int studentId, int groupId) throws ServiceException;

    List<StudentDto> getAllStudents() throws ServiceException;

    List<StudentDto> getStudentsByCourse(int courseId) throws ServiceException;

    List<StudentDto> getStudentsByNameAndCourse(String studentName, Integer courseId) throws ServiceException;
}
