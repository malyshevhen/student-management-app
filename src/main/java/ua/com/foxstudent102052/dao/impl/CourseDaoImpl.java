package ua.com.foxstudent102052.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.dao.mapper.CourseDaoMapper;
import ua.com.foxstudent102052.model.entity.Course;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CourseDaoImpl implements CourseDao {
    private final CustomDataSource dataSource;

    public CourseDaoImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addCourse(Course course) {
        var query = """
            INSERT
            INTO courses (course_name, course_description)
            VALUES (?, ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Course> getCourse(int courseId) {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses
            WHERE course_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);

            var courseResultSet = statement.executeQuery();

            if (courseResultSet.next()) {
                var course = CourseDaoMapper.mapToCourse(courseResultSet);

                return Optional.of(course);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Course> getCourse(String courseName) {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses
            WHERE course_name = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setString(1, courseName);

            var courseResultSet = statement.executeQuery();

            if (courseResultSet.next()) {
                var course = CourseDaoMapper.mapToCourse(courseResultSet);

                return Optional.of(course);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Course> getCourses() {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var coursesResultSet = statement.executeQuery(query)) {

            return CourseDaoMapper.mapToCourses(coursesResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Course> getCourses(int studentId) {
        var query = """
            SELECT course_id, course_name, course_description
            FROM courses
            WHERE course_id
            IN (
                SELECT course_id
                FROM students_courses
                WHERE student_id = ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);

            var coursesResultSet = statement.executeQuery();

            return CourseDaoMapper.mapToCourses(coursesResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }
}
