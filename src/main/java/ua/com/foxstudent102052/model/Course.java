package ua.com.foxstudent102052.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    private int courseId;
    private String courseName;
    private String courseDescription;
}
