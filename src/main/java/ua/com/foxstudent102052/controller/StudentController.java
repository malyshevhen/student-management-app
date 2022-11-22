package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.ServiceException;
import ua.com.foxstudent102052.service.StudentService;

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
            var studentDtoList = studentService.getAllStudents();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<StudentDto> getStudentsByNameAndCourse(String studentName, Integer courseId)
        throws ControllerException {
        try {
            var studentDtoList = studentService.getStudentsByNameAndCourse(studentName, courseId);

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    private List<StudentDto> updateStudentsGroupAndCourse(List<StudentDto> studentDtoList) {

        studentDtoList.forEach(studentDto -> {

            try {
                setStudentsGroup(studentDto);
                setStudentsCourse(studentDto);
            } catch (ControllerException e) {
                log.error(e.getMessage());
            }
        });

        return studentDtoList;
    }

    private void setStudentsCourse(StudentDto studentDto) throws ControllerException {
        int id = studentDto.getId();

        try {
            studentDto.setCoursesList(courseService.getCoursesByStudentId(id));
        } catch (ServiceException e) {
            String msg = "Student with id: %d has no courses".formatted(id);
            log.error(msg);

            throw new ControllerException(e);
        }
    }

    private void setStudentsGroup(StudentDto studentDto) throws ControllerException {
        try {
            int groupId = studentDto.getGroupId();
            var groupById = groupService.getGroupById(groupId);
            studentDto.setGroup(groupById.getName());
        } catch (ServiceException e) {
            String msg = "Student with id: %d has no group".formatted(studentDto.getId());
            log.error(msg);

            throw new ControllerException(e);
        }
    }
}
