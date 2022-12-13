package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.controller.exceptions.ControllerException;
import ua.com.foxstudent102052.dto.GroupDto;
import ua.com.foxstudent102052.dto.StudentDto;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.CourseService;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;

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
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);

            throw new ControllerException(e);
        }
    }

    public void addStudentToCourse(int studentId, int courseId) throws ControllerException {
        try {
            studentService.addStudentToCourse(studentId, courseId);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);

            throw new ControllerException(e);
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws ControllerException {
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
        } catch (ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<StudentDto> getAllStudents() throws ControllerException {
        try {
            var studentDtoList = studentService.getStudents();

            return setStudentsRelations(studentDtoList);
        } catch (ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<StudentDto> getStudents(String studentName, Integer courseId)
        throws ControllerException {
        try {
            var studentDtoList = studentService.getStudents(studentName, courseId);

            return setStudentsRelations(studentDtoList);
        } catch (ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    private List<StudentDto> setStudentsRelations(List<StudentDto> studentDtoList) {
        return studentDtoList.stream()
            .map(this::setStudentsGroup)
            .map(this::getSetCoursesList)
            .toList();
    }

    private StudentDto getSetCoursesList(StudentDto studentDto) {
        try {
            studentDto.setCoursesList(courseService.getCourses(studentDto.getId()));

            return studentDto;
        } catch (ServiceException e) {
            log.info("Student with id: {} hase no courses", studentDto.getId());

            return studentDto;
        }
    }

    private StudentDto setStudentsGroup(StudentDto studentDto) {
        int groupId = studentDto.getGroup().getId();
        GroupDto groupById = null;
        try {
            groupById = groupService.getGroup(groupId);
            studentDto.setGroup(groupById);

            return studentDto;
        } catch (ServiceException e) {
            log.info("Student with id: {} hase no group", studentDto.getId());

            return studentDto;
        }
    }
}
