package ua.com.foxstudent102052.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.dao.mapper.CourseRowMapper;
import ua.com.foxstudent102052.model.entity.Course;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseDaoImpl implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addCourse(Course course) {
        var query = """
            INSERT
            INTO courses (course_name, course_description)
            VALUES (?, ?);""";

        jdbcTemplate.update(query, course.getName(), course.getDescription());
    }

    @Override
    public Optional<Course> getCourseById(int courseId) {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses
            WHERE course_id = ?;""";

        return jdbcTemplate.query(query, new CourseRowMapper(), courseId)
            .stream()
            .findFirst();
    }

    @Override
    public Optional<Course> getCourseByName(String courseName) {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses
            WHERE course_name = ?;""";

        return jdbcTemplate.query(query, new CourseRowMapper(), courseName)
            .stream()
            .findFirst();
    }

    @Override
    public List<Course> getAll() {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses;""";

        return jdbcTemplate.query(query, new CourseRowMapper());
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses
            WHERE course_id
            IN (
                SELECT course_id
                FROM students_courses
                WHERE student_id = ?);""";

        return jdbcTemplate.query(query, new CourseRowMapper(), studentId);
    }
}
