package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    public void addStudent(StudentDto studentDto) throws DataAccessException {
        studentService.addStudent(studentDto);
    }

    public void removeStudent(int studentId) throws NoSuchElementException, DataAccessException {
        studentService.removeStudent(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) throws NoSuchElementException, DataAccessException {
        studentService.addStudentToCourse(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId)
            throws NoSuchElementException, DataAccessException {
        studentService.removeStudentFromCourse(studentId, courseId);
    }

    public List<StudentDto> getAllStudents() throws NoSuchElementException, DataAccessException {
        return studentService.getAll()
                .stream()
                .map(this::setStudentsGroup)
                .map(this::setStudentsCourseList)
                .toList();
    }

    public List<StudentDto> getStudents(String studentName, Integer courseId)
            throws NoSuchElementException, DataAccessException {
        return studentService.getStudentsByNameAndCourse(studentName, courseId)
                .stream()
                .map(this::setStudentsGroup)
                .map(this::setStudentsCourseList)
                .toList();
    }

    private StudentDto setStudentsCourseList(StudentDto studentDto) {
        try {
            int studentId = studentDto.getStudentId();
            var courseDtoList = courseService.getCoursesByStudent(studentId);
            studentDto.setCoursesList(courseDtoList);

            return studentDto;
        } catch (NoSuchElementException | DataAccessException e) {
            return studentDto;
        }
    }

    private StudentDto setStudentsGroup(StudentDto studentDto) {
        try {
            int groupId = studentDto.getGroup().getGroupId();
            var studentsGroup = groupService.getGroupById(groupId);
            studentDto.setGroup(studentsGroup);

            return studentDto;
        } catch (NullPointerException | NoSuchElementException | DataAccessException e) {
            return studentDto;
        }
    }
}
