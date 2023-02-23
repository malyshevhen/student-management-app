package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.foxstudent102052.model.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select distinct s from Student s left join s.courses c where c.id = :courseId")
    List<Student> findByCourseId(@Param("courseId") Long courseId);

    @Query("select distinct s from Student s where s.group.id = :groupId")
    List<Student> findByGroupId(@Param("groupId") Long groupId);

    @Query("select distinct s from Student s left join s.courses c where c.id = :courseId and s.firstName = :studentName")
    List<Student> findByNameAndCourseId(@Param("studentName") String studentName, @Param("courseId") Long courseId);

}
