package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.StudentService;
import ua.com.foxstudent102052.service.exception.NoSuchCourseExistsException;
import ua.com.foxstudent102052.service.exception.NoSuchGroupExistsException;
import ua.com.foxstudent102052.service.exception.NoSuchStudentExistsException;
import ua.com.foxstudent102052.service.exception.StudentAlreadyExistException;

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


    public void addStudent(StudentDto studentDto) {
        try {
            studentService.addStudent(studentDto);
        } catch (StudentAlreadyExistException e) {
            log.error(e.getMessage());
        }
    }

    public void removeStudent(int studentId) {
        try {
            studentService.removeStudent(studentId);
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }
    }

    public void addStudentToCourse(int studentId, int courseId) {
        try {
            studentService.addStudentToCourse(studentId, courseId);
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }
    }

    public List<StudentDto> getAllStudents() {
        try {
            var studentDtoList = studentService.getAllStudents();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }
    
    public List<StudentDto> getStudentsByCourseNameAndGroupId(String studentName, Integer courseId) {
        try {
            var studentDtoList = studentService.getStudentsByCourseNameAndGroupId(studentName, courseId);

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }
    
    private List<StudentDto> updateStudentsGroupAndCourse(List<StudentDto> studentDtoList) {
        studentDtoList.forEach(studentDto -> {
            setStudentsGroup(studentDto);
            setStudentsCourse(studentDto);
        });

        return studentDtoList;
    }

    private void setStudentsCourse(StudentDto studentDto) {
        int id = studentDto.getId();
        try {
            studentDto.setCoursesList(
                courseService.getCoursesByStudentId(id));
        } catch (NoSuchCourseExistsException e) {
            log.error("Student with id: %d has no courses".formatted(id));
        }
    }

    private void setStudentsGroup(StudentDto studentDto) {
        try {
            int groupId = studentDto.getGroupId();
            var groupById = groupService.getGroupById(groupId);
            studentDto.setGroup(groupById.getName());
        } catch (NoSuchGroupExistsException e) {
            log.error("Student with id: %d has no group".formatted(studentDto.getId()));
        }
    }
}
