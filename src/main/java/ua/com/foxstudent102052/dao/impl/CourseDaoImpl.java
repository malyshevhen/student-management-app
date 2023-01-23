package ua.com.foxstudent102052.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.entity.Course;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseDaoImpl implements CourseDao {
    private final JdbcTemplate jdbcTemplate;

    @Qualifier("courseRowMapper")
    private final RowMapper<Course> courseRowMapper;

    @Override
    public void addCourse(Course course) throws DataAccessException {
        var query = """
                INSERT
                INTO courses (course_name, course_description)
                VALUES (?, ?);""";

        jdbcTemplate.update(query, course.getCourseName(), course.getCourseDescription());
    }

    @Override
    public Optional<Course> getCourseById(int courseId) throws DataAccessException {
        var query = """
                SELECT course_id, course_name, course_description
                FROM courses
                WHERE course_id = ?;""";

        return jdbcTemplate.query(query, courseRowMapper, courseId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Course> getCourseByName(String courseName) throws DataAccessException {
        var query = """
                SELECT course_id, course_name, course_description
                FROM courses
                WHERE course_name = ?;""";

        return jdbcTemplate.query(query, courseRowMapper, courseName)
                .stream()
                .findFirst();
    }

    @Override
    public List<Course> getAll() throws DataAccessException {
        var query = """
                SELECT course_id, course_name, course_description
                FROM courses;""";

        return jdbcTemplate.query(query, courseRowMapper);
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) throws DataAccessException {
        var query = """
                SELECT course_id, course_name, course_description
                FROM courses
                WHERE course_id
                IN (
                    SELECT course_id
                    FROM students_courses
                    WHERE student_id = ?);""";

        return jdbcTemplate.query(query, courseRowMapper, studentId);
    }
}
