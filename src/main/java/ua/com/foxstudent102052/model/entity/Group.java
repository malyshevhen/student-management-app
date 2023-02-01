package ua.com.foxstudent102052.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groups")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId;

    @Column(nullable = false, unique = true)
    private String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return "Group [groupId=" + groupId + ", groupName=" + groupName + "]";
    }
}
