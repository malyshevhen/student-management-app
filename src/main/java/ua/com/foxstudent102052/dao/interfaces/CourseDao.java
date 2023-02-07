package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxstudent102052.model.entity.Course;

public interface CourseDao extends JpaRepository<Course, Integer> {

    Optional<Course> findByName(String courseName);

    List<Course> findByStudentId(int studentId);
}
