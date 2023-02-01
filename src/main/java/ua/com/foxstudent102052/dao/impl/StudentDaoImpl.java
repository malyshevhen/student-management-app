package ua.com.foxstudent102052.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

@Repository
public class StudentDaoImpl implements StudentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addStudent(Student student) throws DataAccessException {
        entityManager.persist(student);
    }

    @Override
    public void removeStudent(int studentId) throws DataAccessException {
        var student = entityManager.find(Student.class, studentId);
        entityManager.remove(student);
    }

    @Override
    public void addStudentToGroup(int studentId, int groupId) {
        var group = entityManager.find(Group.class, groupId);
        var student = entityManager.find(Student.class, studentId);

        student.setGroup(group);

        entityManager.persist(student);
        entityManager.persist(group);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws DataAccessException {
        var student = entityManager.find(Student.class, studentId);
        var course = entityManager.find(Course.class, courseId);
        student.addCourse(course);

        entityManager.persist(student);
        entityManager.persist(course);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws DataAccessException {
        var student = entityManager.find(Student.class, studentId);
        var course = entityManager.find(Course.class, courseId);
        student.removeCourse(course);

        entityManager.persist(student);
        entityManager.persist(course);
    }

    @Override
    public Optional<Student> getStudent(int studentId) throws DataAccessException {
        var student = entityManager.find(Student.class, studentId);

        return Optional.ofNullable(student);
    }

    @Override
    public List<Student> getAll() throws DataAccessException {
        var query = """
                SELECT s
                FROM Student s""";

        return entityManager.createQuery(query, Student.class).getResultList();
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) throws DataAccessException {
        var courses = entityManager.find(Course.class, courseId);

        return courses.getStudents();
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) throws DataAccessException {
        var group = entityManager.find(Group.class, groupId);

        return group.getStudents();
    }

    @Override
    public List<Student> getStudentsByNameAndCourse(String studentName, int courseId) throws DataAccessException {
        var course = entityManager.find(Course.class, courseId);

        return course.getStudents()
                .stream()
                .filter(student -> student.getFirstName().equals(studentName))
                .toList();
    }
}
