package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.foxstudent102052.model.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select s from Student s left join s.courses c where c.courseId = :courseId")
    List<Student> findByCourseId(@Param("courseId") int courseId);

    @Query("select s from Student s where s.group.groupId = :groupId")
    List<Student> findByGroupId(@Param("groupId") int groupId);

    @Query("select s from Student s left join s.courses c where c.courseId = :courseId and s.firstName = :studentName")
    List<Student> findByNameAndCourseId(@Param("studentName") String studentName, @Param("courseId") int courseId);

}
