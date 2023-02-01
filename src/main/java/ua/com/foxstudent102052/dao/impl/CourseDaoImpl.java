package ua.com.foxstudent102052.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Student;

@Repository
@Transactional
public class CourseDaoImpl implements CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addCourse(Course course) throws DataAccessException {
        entityManager.persist(course);
    }

    @Override
    public Optional<Course> getCourseById(int courseId) throws DataAccessException {
        var course = entityManager.find(Course.class, courseId);

        return Optional.ofNullable(course);
    }

    @Override
    public Optional<Course> getCourseByName(String courseName) throws DataAccessException {
        var query = """
                SELECT c
                FROM Course c
                WHERE c.courseName =: courseName""";

        var course = entityManager.createQuery(query, Course.class).setParameter("courseName", courseName)
                .getSingleResult();

        return Optional.ofNullable(course);
    }

    @Override
    public List<Course> getAll() throws DataAccessException {
        var query = """
                SELECT c
                FROM Course c""";

        return entityManager.createQuery(query, Course.class).getResultList();
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) throws DataAccessException {
        var student = entityManager.find(Student.class, studentId);

        return student.getCourses();
    }
}
