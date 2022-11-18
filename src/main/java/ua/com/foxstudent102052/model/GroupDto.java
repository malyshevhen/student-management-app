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
public class GroupDto {
    private int id;
    private String name;
    private List<StudentDto> studentList;
}
