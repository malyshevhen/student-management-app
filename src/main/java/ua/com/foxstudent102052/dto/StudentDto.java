package ua.com.foxstudent102052.dto;

import lombok.*;

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
    private List<CourseDto> coursesList;
}
