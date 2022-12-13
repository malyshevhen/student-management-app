package ua.com.foxstudent102052.mapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dto.GroupDto;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.dto.StudentDto;

import java.util.List;

@Slf4j
public class StudentMapper {

    private StudentMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static StudentDto toStudentDto(Student student) {
        try {
            return StudentDto.builder()
                .id(student.studentId())
                .group(GroupDto.builder().id(student.groupId()).build())
                .firstName(student.firstName())
                .lastName(student.lastName())
                .coursesList(List.of())
                .build();
        } catch (NullPointerException e) {
            log.error("StudentMapper.studentToDto() - NullPointerException: " + e.getMessage());

            return new StudentDto();
        }
    }

    public static Student toStudent(StudentDto studentDto) {
        try {
            return Student.builder()
                .studentId(studentDto.getId())
                .groupId(studentDto.getGroup().getId())
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .build();
        } catch (NullPointerException e) {
            log.error("StudentMapper.dtoToStudent() - NullPointerException: " + e.getMessage());

            return Student.builder().build();
        }
    }
}
