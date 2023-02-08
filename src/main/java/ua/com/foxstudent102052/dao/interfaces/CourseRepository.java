package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxstudent102052.model.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByCourseName(String courseName);

    @Query("select c from Course c left join fetch c.students s where s.studentId = :studentId")
    List<Course> findByStudentId(@Param("studentId") int studentId);
}
