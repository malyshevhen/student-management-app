package ua.com.foxstudent102052.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private int id;
    private String name;
    private List<StudentDto> studentList;

    public GroupDto(String name) {
        this.name = name;
    }

    public GroupDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
