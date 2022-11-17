package ua.com.foxstudent102052.model;

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
    private int groupId;
    private String group;
    private String fistName;
    private String lastName;
    private List<CourseDto> coursesList;   
}
