package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.controller.exceptions.ControllerException;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    public StudentController(StudentService studentService, GroupService groupService, CourseService courseService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.courseService = courseService;
    }

    public void addStudent(StudentDto studentDto) throws ControllerException {
        try {
            studentService.addStudent(studentDto);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);

            throw new ControllerException(e);
        }
    }

    public void removeStudent(int studentId) throws ControllerException {
        try {
            studentService.removeStudent(studentId);
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage(), e);

            throw new ControllerException(e);
        }
    }

    public void addStudentToCourse(int studentId, int courseId) throws ControllerException {
        try {
            studentService.addStudentToCourse(studentId, courseId);
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage(), e);

            throw new ControllerException(e);
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws ControllerException {
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<StudentDto> getAllStudents() throws ControllerException {
        try {
            return studentService.getStudents()
                .stream()
                .map(this::setStudentsGroup)
                .map(this::setStudentsCourseList)
                .toList();
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<StudentDto> getStudents(String studentName, Integer courseId)
        throws ControllerException {
        try {
            return studentService.getStudents(studentName, courseId)
                .stream()
                .map(this::setStudentsGroup)
                .map(this::setStudentsCourseList)
                .toList();
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    private StudentDto setStudentsCourseList(StudentDto studentDto) {
        int studentId = studentDto.getId();

        try {
            var courseDtoList = courseService.getCourses(studentId);

            studentDto.setCoursesList(courseDtoList);

            return studentDto;
        } catch (NoSuchElementException | ServiceException e) {
            log.info("Student with id: {} hase no courses", studentId);

            return studentDto;
        }
    }

    private StudentDto setStudentsGroup(StudentDto studentDto) {
        try {
            int groupId = studentDto.getGroup().getId();
            var studentsGroup = groupService.getGroup(groupId);
            studentDto.setGroup(studentsGroup);

            return studentDto;
        } catch (NullPointerException | NoSuchElementException | ServiceException e) {
            log.info("Student with id: {} hase no group", studentDto.getId());

            return studentDto;
        }
    }
}
