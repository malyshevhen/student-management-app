package ua.com.foxstudent102052.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private int id;
    private int groupId;
    private String group;
    private String fistName;
    private String lastName;
    private List<CourseDto> coursesList;

    public StudentDto(int groupId, String fistName, String lastName) {
        this.groupId = groupId;
        this.fistName = fistName;
        this.lastName = lastName;
    }

    public StudentDto(int id, int groupId, String fistName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.fistName = fistName;
        this.lastName = lastName;
    }     
}
