package ua.com.foxstudent102052.mapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

@Slf4j
public class StudentMapper {

    private StudentMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static StudentDto toDto(Student student) {
        try {
            return new StudentDto(
                    student.getStudentId(),
                    student.getGroupId(),
                    "",
                    student.getFirstName(),
                    student.getLastName(),
                    List.of());

        } catch (NullPointerException e) {
            log.error("StudentMapper.studentToDto() - NullPointerException: " + e.getMessage());
            return new StudentDto();
        }
    }

    public static Student toStudent(StudentDto studentDto) {
        try {
            return new Student(
                    studentDto.getId(),
                    studentDto.getGroupId(),
                    studentDto.getFirstName(),
                    studentDto.getLastName());

        } catch (NullPointerException e) {
            log.error("StudentMapper.dtoToStudent() - NullPointerException: " + e.getMessage());
            return new Student();
        }
    }
}
