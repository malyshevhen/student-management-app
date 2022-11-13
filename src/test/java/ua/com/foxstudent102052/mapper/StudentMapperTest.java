package ua.com.foxstudent102052.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    @DisplayName("Method 'studentToDto' should return StudentDto object")
    @ParameterizedTest
    @MethodSource
    void studentToDto(Student student, StudentDto expected) {
        StudentDto actual = StudentMapper.studentToDto(student);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> studentToDto() {
        return Stream.of(
            Arguments.of(
                new Student(1, 10, "Ivan", "Ivanenko"),
                new StudentDto(1, 10, "", "Ivan", "Ivanenko", List.of())
            ),
            Arguments.of(
                new Student(2, 1, "Petro", "Petrenko"),
                new StudentDto(2, 1, "", "Petro", "Petrenko", List.of())
            ),
            Arguments.of(
                new Student(0,0, null, null),
                new StudentDto(0, 0, "", null, null, List.of())
            ),
            Arguments.of(
                null,
                new StudentDto()
            )
        );
    }

    @DisplayName("Method 'dtoToStudent' should return Student object")
    @ParameterizedTest
    @MethodSource
    void dtoToStudent(StudentDto studentDto, Student expected) {
        Student actual = StudentMapper.dtoToStudent(studentDto);
        
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dtoToStudent() {
        return Stream.of(
            Arguments.of(
                new StudentDto(1, 10, "", "Ivan", "Ivanenko", List.of()),
                new Student(1, 10, "Ivan", "Ivanenko")
            ),
            Arguments.of(
                new StudentDto(2, 1, "", "Petro", "Petrenko", List.of()),
                new Student(2, 1, "Petro", "Petrenko")
            ),
            Arguments.of(
                new StudentDto(0, 0, "", null, null, List.of()),
                new Student(0,0, null, null)
            ),
            Arguments.of(
                null,
                new Student()
            )
        );
    }
}
