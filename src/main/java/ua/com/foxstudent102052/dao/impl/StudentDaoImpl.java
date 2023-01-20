package ua.com.foxstudent102052.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.dao.mapper.StudentRowMapper;
import ua.com.foxstudent102052.model.entity.Student;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentDaoImpl implements StudentDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addStudent(Student student) {
        var query = """
                INSERT INTO students (
                    group_id, first_name, last_name)
                VALUES (?, ?, ?);""";

        jdbcTemplate.update(query, student.getGroupId(), student.getFirstName(), student.getLastName());
    }

    @Override
    public void removeStudent(int studentId) {
        var query = """
                DELETE FROM students_courses
                WHERE student_id = ?;
                DELETE
                FROM students
                WHERE student_id = ?;""";

        jdbcTemplate.update(query, studentId, studentId);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        var query = """
                INSERT INTO students_courses (
                    student_id,
                    course_id)
                VALUES (?, ?);""";

        jdbcTemplate.update(query, studentId, courseId);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        var query = """
                DELETE
                FROM students_courses
                WHERE student_id = ?
                AND course_id = ?;""";

        jdbcTemplate.update(query, studentId, courseId);
    }

    @Override
    public Optional<Student> getStudent(int id) {
        var query = """
                SELECT student_id, group_id, first_name, last_name
                FROM students
                WHERE student_id = ?;""";

        return jdbcTemplate.query(query, new StudentRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Student> getAll() {
        var query = """
                SELECT student_id, group_id, first_name, last_name
                FROM students;""";

        return jdbcTemplate.query(query, new StudentRowMapper());
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) {
        var query = """
                SELECT student_id, group_id, first_name, last_name
                FROM students
                WHERE student_id IN (
                    SELECT student_id
                    FROM students_courses
                    WHERE course_id = ?);""";

        return jdbcTemplate.query(query, new StudentRowMapper(), courseId);
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) {
        var query = """
                SELECT student_id, group_id, first_name, last_name
                FROM students
                WHERE group_id = ? ;""";

        return jdbcTemplate.query(query, new StudentRowMapper(), groupId);
    }

    @Override
    public List<Student> getStudentsByNameAndCourse(String studentName, int courseId) {
        var query = """
                SELECT student_id, group_id, first_name, last_name
                FROM students
                WHERE student_id IN (
                    SELECT student_id
                    FROM students_courses
                    WHERE course_id = ?)
                AND first_name = ?;""";

        return jdbcTemplate.query(query, new StudentRowMapper(), courseId, studentName);
    }
}
