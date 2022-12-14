package ua.com.foxstudent102052.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.CourseRepository;
import ua.com.foxstudent102052.datasource.interfaces.CustomDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CourseRepositoryImpl implements CourseRepository {
    private static final String QUERY_EXECUTED_SUCCESSFULLY = "Query executed successfully";

    private final CustomDataSource dataSource;

    public CourseRepositoryImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addCourse(Course course) throws RepositoryException {
        var query = """
            INSERT
            INTO courses (course_name, course_description)
            VALUES (?, ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setString(1, course.courseName());
            statement.setString(2, course.courseDescription());
            statement.executeUpdate();
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (SQLException e) {
            log.error("Error while adding course", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Course> getAllCourses() throws RepositoryException {
        var query = "SELECT * FROM courses;";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var coursesResultSet = statement.executeQuery(query)) {

            return getCoursesFromResultSet(coursesResultSet);

        } catch (SQLException e) {
            log.error("Error while getting all courses", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public Course getCourseById(int courseId) throws RepositoryException {
        var query = """
            SELECT *
            FROM courses
            WHERE course_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            var courseResultSet = statement.executeQuery();

            if (courseResultSet.next()) {
                return getCourseFromResultSet(courseResultSet);
            } else {
                throw new RepositoryException("No such course in DB");
            }
        } catch (SQLException e) {
            log.error("Error while getting course by id", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) throws RepositoryException {
        var query = """
            SELECT *
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

            return getCoursesFromResultSet(coursesResultSet);
        } catch (SQLException e) {
            log.error("Error while getting courses by student id", e);

            throw new RepositoryException(e);
        }
    }

    private static Course getCourseFromResultSet(ResultSet courseResultSet) throws SQLException {
        return Course.builder()
            .courseId(courseResultSet.getInt(1))
            .courseName(courseResultSet.getString(2))
            .courseDescription(courseResultSet.getString(3))
            .build();
    }

    private static ArrayList<Course> getCoursesFromResultSet(ResultSet coursesResultSet) throws SQLException {
        var courses = new ArrayList<Course>();
        while (coursesResultSet.next()) {
            var course = getCourseFromResultSet(coursesResultSet);
            courses.add(course);
        }

        return courses;
    }
}
