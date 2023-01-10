package ua.com.foxstudent102052.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Student {
    private int id;
    private int groupId;
    private String firstName;
    private String lastName;
}
