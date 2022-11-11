package ua.com.foxstudent102052.mapper;

import lombok.NonNull;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

public class StudentMapper {

    private StudentMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static StudentDto studentToDto(@NonNull Student student) {
        return new StudentDto(
                student.getStudentId(),
                student.getGroupId(),
                "",
                student.getFirstName(),
                student.getLastName(),
                List.of());
    }

    public static Student dtoToStudent(@NonNull StudentDto studentDto) {
        return new Student(
                studentDto.getGroupId(),
                studentDto.getFistName(),
                studentDto.getLastName());
    }
}
