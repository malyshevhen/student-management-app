package ua.com.foxstudent102052.model.mapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.model.dto.StudentDto;

import java.util.List;

@Slf4j
public class StudentModelMapper {

    private StudentModelMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static StudentDto toStudentDto(Student student) {
        try {
            return StudentDto.builder()
                .id(student.getId())
                .group(GroupDto.builder().id(student.getGroupId()).build())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
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
                .id(studentDto.getId())
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
