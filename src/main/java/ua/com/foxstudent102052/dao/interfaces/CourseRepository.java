package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.foxstudent102052.model.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String courseName);

    @Query("select distinct c from Course c left join c.students s where s.id = :studentId")
    List<Course> findByStudentId(@Param("studentId") Long studentId);
}
