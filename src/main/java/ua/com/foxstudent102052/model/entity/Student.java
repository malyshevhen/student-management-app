package ua.com.foxstudent102052.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Setter(value = AccessLevel.PRIVATE)
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "students_courses", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();

    public void setGroup(Group group) {
        group.addStudent(this);
        this.group = group;
    }

    public void addCourse(Course course) {
        course.addStudent(this);
        courses.add(course);
    }

    public void removeCourse(Course course) {
        course.removeStudent(this);
        courses.remove(course);
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", group=" + group + ", firstName=" + firstName + ", lastName="
                + lastName + "]";
    }
}
