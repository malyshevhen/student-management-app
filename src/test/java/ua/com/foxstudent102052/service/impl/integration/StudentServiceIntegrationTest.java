package ua.com.foxstudent102052.service.impl.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxstudent102052.AppRunner;
import ua.com.foxstudent102052.dao.interfaces.CourseRepository;
import ua.com.foxstudent102052.dao.interfaces.StudentRepository;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.service.impl.StudentServiceImpl;
import ua.com.foxstudent102052.service.interfaces.StudentService;

@SpringBootTest
@Transactional
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.datasource.url=jdbc:tc:postgresql:15://students_db")
public class StudentServiceIntegrationTest {
    private final StudentService service;

    @MockBean
    private AppRunner appRunner;

    @Autowired
    public StudentServiceIntegrationTest(StudentRepository studentDao, CourseRepository courseDao,
            ModelMapper modelMapper) {
        this.service = new StudentServiceImpl(studentDao, courseDao, modelMapper);
    }

    @Test
    void Method_AddStudentToCourse_ShouldAddExistingStudentToExistingCourse() {
        int expected = 1;

        service.addStudentToCourse(1L, 2L);

        var actual = service.getStudentsByCourse(1L)
                .stream()
                .filter(studentDto -> studentDto.getId() == 1L)
                .findFirst()
                .map(StudentDto::getId)
                .orElseThrow();

        assertEquals(expected, actual);
    }

    @Test
    void Method_RemoveStudentToCourse_ShouldRemoveExistingStudentFromExistingCourse() {
        service.removeStudentFromCourse(1L, 1L);

        boolean notRemoved = service.getStudentsByCourse(1L)
                .stream()
                .mapToLong(StudentDto::getId)
                .anyMatch(id -> id == 1L);

        assertFalse(notRemoved);
    }
}
