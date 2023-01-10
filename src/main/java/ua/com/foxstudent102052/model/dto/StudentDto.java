package ua.com.foxstudent102052.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private int id;
    private String firstName;
    private String lastName;
    private GroupDto group;
    private List<CourseDto> coursesList = List.of();
}
