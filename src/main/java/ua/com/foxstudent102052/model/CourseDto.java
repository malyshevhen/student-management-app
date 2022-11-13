package ua.com.foxstudent102052.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private int id;
    private String name;
    private String description;
    private List<StudentDto> studentsList;

    public CourseDto(String name, String description) {
        this.name = name;
        this.description = description;
        this.studentsList = List.of();
    }

    public CourseDto(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.studentsList = List.of();
    }
}
