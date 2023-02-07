package ua.com.foxstudent102052.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxstudent102052.model.entity.Student;

public interface StudentDao extends JpaRepository<Student, Integer> {

    List<Student> findByCourseId(int courseId);

    List<Student> findByGroupId(int groupId);

    List<Student> findByNameAndCourseId(String studentName, int courseId);

}
