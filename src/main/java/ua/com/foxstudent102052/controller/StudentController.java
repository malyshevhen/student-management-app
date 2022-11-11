package ua.com.foxstudent102052.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.CourseMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.CourseRepositoryImpl;
import ua.com.foxstudent102052.repository.GroupRepositoryImpl;
import ua.com.foxstudent102052.repository.StudentRepositoryImpl;
import ua.com.foxstudent102052.service.*;

import java.util.List;

@Slf4j
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    public StudentController() {
        this.studentService = new StudentServiceImpl(StudentRepositoryImpl.getInstance());
        this.groupService = new GroupServiceImpl(GroupRepositoryImpl.getInstance());
        this.courseService = new CourseServiceImpl(CourseRepositoryImpl.getInstance());
    }

    public void addStudent(StudentDto studentDto) {
        try {
            studentService.addStudent(StudentMapper.dtoToStudent(studentDto));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void removeStudent(int id) {
        try {
            studentService.removeStudent(id);
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

    public void updateStudentsGroup(int studentId, int groupId) {
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
            StudentDto studentDto = StudentMapper.studentToDto(studentService.getStudentById(studentId));

            setStudentsGroup(studentDto);

            try {
                studentDto.setCoursesList(
                    studentService.getCoursesByStudentId(studentId)
                        .stream()
                        .map(CourseMapper::courseToDto)
                        .toList());
                
            } catch (IllegalArgumentException e) {
                log.info("Student with id: " + studentDto.getId() + " has no courses");
            }

            return studentDto;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        
        return new StudentDto();
    }

    public List<StudentDto> getAllStudents() {
        try {
            var studentDtoList = studentService.getAllStudents().stream().map(StudentMapper::studentToDto).toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByGroupId(int groupId) {
        try {
            var studentDtoList = groupService.getStudentsByGroup(groupId).stream().map(StudentMapper::studentToDto).toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<StudentDto> getStudentsByLastName(String lastName) {
        try {
            var studentDtoList = studentService.getStudentsByLastName(lastName).stream().map(StudentMapper::studentToDto)
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
            var studentDtoList = studentService.getStudentsBySurnameAndName(firstName, lastName)
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
            var studentDtoList = courseService.getStudentsByCourse(studentCourseId).stream()
                .map(StudentMapper::studentToDto).toList();

            return updateStudentsGroupAndCourse(studentDtoList);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }
    
    private List<StudentDto> updateStudentsGroupAndCourse(@NonNull List<StudentDto> studentDtoList) {
        studentDtoList.forEach(studentDto -> {
            setStudentsGroup(studentDto);
            setStudentsCourse(studentDto);
        });

        return studentDtoList;
    }

    private void setStudentsCourse(StudentDto studentDto) {
        try {
            studentDto.setCoursesList(
                studentService.getCoursesByStudentId(studentDto.getId()).stream().map(CourseMapper::courseToDto)
                    .toList());
        } catch (IllegalArgumentException e) {
            log.info("Student with id: " + studentDto.getId() + " has no courses");
        }
    }

    private void setStudentsGroup(StudentDto studentDto) {
        try {
            studentDto.setGroup(groupService.getGroupById(studentDto.getGroupId()).getGroupName());
        } catch (IllegalArgumentException e) {
            log.info("Student with id: " + studentDto.getId() + " has no group");
        }
    }
}
