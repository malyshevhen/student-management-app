package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/add")
    public void addStudent(StudentDto studentDto) {
        studentService.addStudent(studentDto);
    }

    @DeleteMapping("/{id}")
    public void removeStudent(@PathVariable Long studentId) {
        studentService.removeStudent(studentId);
    }

    @PostMapping("/add-course/{studentId}/{courseId}")
    public void addStudentToCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.addStudentToCourse(studentId, courseId);
    }

    @PostMapping("/remove-course/{studentId}/{courseId}")
    public void removeStudentFromCourse(
        @PathVariable Long studentId,
        @PathVariable Long courseId
    ) throws NoSuchElementException, DataAccessException {
        studentService.removeStudentFromCourse(studentId, courseId);
    }

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping("/{name}/{courseId}")
    public List<StudentDto> getStudents(@PathVariable String name, @PathVariable Long courseId) {
        return studentService.getStudentsByNameAndCourse(name, courseId);
    }
}
