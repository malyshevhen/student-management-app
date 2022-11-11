package ua.com.foxstudent102052.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private int groupId;
    private String groupName;
    
    public Group(String groupName) {
        this.groupName = groupName;
    }
}
