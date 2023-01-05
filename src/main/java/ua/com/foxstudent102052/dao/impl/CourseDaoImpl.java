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
    public static final String TABLE_NAME = "courses";
    public static final String JOIN_TABLE_NAME = "students_courses";
    public static final String TABLE_COLUMN_1 = "course_id";
    public static final String TABLE_COLUMN_2 = "course_name";
    public static final String TABLE_COLUMN_3 = "course_description";
    public static final String JOIN_TABLE_COLUMN_1 = "student_id";

    private final CustomDataSource dataSource;

    public CourseDaoImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addCourse(Course course) {
        var query = """
            INSERT
            INTO $table_name ($col2, $col3)
            VALUES (?, ?);""";

        query = query.replace("$table_name", TABLE_NAME);
        query = query.replace("$col2", TABLE_COLUMN_2);
        query = query.replace("$col3", TABLE_COLUMN_3);


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
            SELECT *
            FROM $table_name
            WHERE $col1 = ?;""";

        query = query.replace("$table_name", TABLE_NAME);
        query = query.replace("$col1", TABLE_COLUMN_1);

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
            SELECT *
            FROM $table_name
            WHERE $col2 = ?;""";

        query = query.replace("$table_name", TABLE_NAME);
        query = query.replace("$col2", TABLE_COLUMN_2);

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
        var query = "SELECT * FROM $table_name;";

        query = query.replace("$table_name", TABLE_NAME);

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
            SELECT *
            FROM $table_name
            WHERE $col1
            IN (
                SELECT $col1
                FROM $join_table_name
                WHERE $join_col1 = ?);""";

        query = query.replace("$table_name", TABLE_NAME);
        query = query.replace("$col1", TABLE_COLUMN_1);
        query = query.replace("$join_table_name", JOIN_TABLE_NAME);
        query = query.replace("$join_col1", JOIN_TABLE_COLUMN_1);

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
