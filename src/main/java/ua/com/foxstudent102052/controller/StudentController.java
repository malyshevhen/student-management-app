package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.CourseService;
import ua.com.foxstudent102052.service.GroupService;
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


    public void addStudent(StudentDto studentDto) {
        try {
            studentService.addStudent(StudentMapper.dtoToStudent(studentDto));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void removeStudent(int studentId) {
        try {
            studentService.removeStudent(studentId);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void addStudentToCourse(int studentId, int courseId) {
        try {
            studentService.addStudentToCourse(studentId, courseId);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void updateStudentFirstName(int studentId, String firstName) {
        try {
            studentService.updateStudentFirstName(studentId, firstName);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void updateStudentsLastName(int studentId, String lastName) {
        try {
            studentService.updateStudentLastName(studentId, lastName);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void addStudentToGroup(int studentId, int groupId) {
        try {
            studentService.updateStudentGroup(studentId, groupId);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void updateStudent(StudentDto studentDto) {
        try {
            studentService.updateStudent(StudentMapper.dtoToStudent(studentDto));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public StudentDto getStudentById(int studentId) {
        try {
            var studentById = studentService.getStudentById(studentId);
            var studentDto = StudentMapper.studentToDto(studentById);
            setStudentsGroup(studentDto);
            setStudentsCourse(studentDto);

            return studentDto;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        
        return new StudentDto();
    }

    public List<StudentDto> getAllStudents() {
        try {
            var studentDtoList = studentService.getAllStudents()
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByGroupId(int groupId) {
        try {
            var studentDtoList = groupService.getStudentsByGroup(groupId)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByLastName(String lastName) {
        try {
            var studentDtoList = studentService.getStudentsByLastName(lastName)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();
            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByName(String name) {
        try {
            var studentDtoList = studentService.getStudentsByName(name)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsBySurnameAndName(String firstName, String lastName) {
        try {
            var studentDtoList = studentService.getStudentsByFullName(firstName, lastName)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByCourseId(int studentCourseId) {
        try {
            var studentDtoList = courseService.getStudentsByCourse(studentCourseId)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
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
                studentService.getCoursesByStudentId(id)
                    .stream()
                    .map(CourseMapper::courseToDto)
                    .toList());
        } catch (IllegalArgumentException e) {
            log.info("Student with id: %d has no courses".formatted(id));
        }
    }

    private void setStudentsGroup(StudentDto studentDto) {
        try {
            int groupId = studentDto.getGroupId();
            var groupById = groupService.getGroupById(groupId);
            studentDto.setGroup(groupById.getGroupName());
        } catch (IllegalArgumentException e) {
            log.info("Student with id: %d has no group".formatted(studentDto.getId()));
        }
    }
}
