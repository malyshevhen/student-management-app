package ua.com.foxstudent102052.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "coursesList")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private int studentId;
    private String firstName;
    private String lastName;
    private GroupDto group;

    @Builder.Default
    private List<CourseDto> coursesList = List.of();
}
