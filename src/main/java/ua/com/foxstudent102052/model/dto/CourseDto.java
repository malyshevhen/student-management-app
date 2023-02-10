package ua.com.foxstudent102052.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "studentList")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private int courseId;
    private String courseName;
    private String courseDescription;

    @Builder.Default
    private List<StudentDto> studentList = List.of();
}
