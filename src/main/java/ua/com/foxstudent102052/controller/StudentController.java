package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.StudentService;

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


    public void addStudent(StudentDto studentDto) throws IllegalArgumentException{
        try {
            studentService.addStudent(studentDto);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            
            throw new IllegalArgumentException(e);
        }
    }

    public void removeStudent(int studentId) throws NoSuchElementException {
        try {
            studentService.removeStudent(studentId);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
            
            throw new NoSuchElementException(e);
        }
    }

    public void addStudentToCourse(int studentId, int courseId) throws NoSuchElementException {
        try {
            studentService.addStudentToCourse(studentId, courseId);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
            
            throw new NoSuchElementException(e);
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) throws NoSuchElementException {
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }

    public List<StudentDto> getAllStudents() throws NoSuchElementException {
        try {
            var studentDtoList = studentService.getAllStudents();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }
    
    public List<StudentDto> getStudentsByCourseNameAndGroupId(String studentName, Integer courseId) throws NoSuchElementException {
        try {
            var studentDtoList = studentService.getStudentsByNameAndCourse(studentName, courseId);

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }
    
    private List<StudentDto> updateStudentsGroupAndCourse(List<StudentDto> studentDtoList) throws NoSuchElementException {
        try {
            studentDtoList.forEach(studentDto -> {
                setStudentsGroup(studentDto);
                setStudentsCourse(studentDto);
            });

            return studentDtoList;
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }

    private void setStudentsCourse(StudentDto studentDto) throws NoSuchElementException {
        int id = studentDto.getId();
        try {
            studentDto.setCoursesList(
                courseService.getCoursesByStudentId(id));
        } catch (NoSuchElementException e) {
            String msg = "Student with id: %d has no courses".formatted(id);
            log.error(msg);


            throw new NoSuchElementException(e);
        }
    }

    private void setStudentsGroup(StudentDto studentDto) throws NoSuchElementException {
        try {
            int groupId = studentDto.getGroupId();
            var groupById = groupService.getGroupById(groupId);
            studentDto.setGroup(groupById.getName());
        } catch (NoSuchElementException e) {
            String msg = "Student with id: %d has no group".formatted(studentDto.getId());
            log.error(msg);
            
            throw new NoSuchElementException(e);
        }
    }
}
