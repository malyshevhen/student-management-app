package ua.com.foxstudent102052.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private int studentId;
    private String firstName;
    private String lastName;
    private GroupDto group;
    private List<CourseDto> coursesList = List.of();
}
