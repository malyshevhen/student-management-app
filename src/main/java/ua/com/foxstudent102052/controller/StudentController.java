package ua.com.foxstudent102052.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto studentDto) {
        var savedStudent = studentService.addStudent(studentDto);

        return ResponseEntity.ok(savedStudent);
    }

    @DeleteMapping("/{id}")
    public void removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
    }


    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
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
