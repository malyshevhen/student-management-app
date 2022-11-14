package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

@Slf4j
public
class DAOFactoryImpl implements DAOFactory {    
    private static final String QUERY_EXEC_SUCCESSFUL = "Query: '{}' executed successfully";
    private static final String CONNECTION_SUCCESSFUL = "Connection to DB successful!";
    private static final String ERROR_MESSAGE = "Error while executing query: {}";

    private static String password = "314159";
    private static String login = "postgres";
    private static String jdbcUrl = "jdbc:postgresql://localhost:5433/students_db";
    
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public void doPost(String query) {

        try (Connection connection = getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement()) {
            log.info(CONNECTION_SUCCESSFUL);
            statement.executeUpdate(query);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }
    }

    @Override
    public Student getStudent(String query) {
        try (Connection connection = getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultStudentSet = statement.executeQuery(query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            if (resultStudentSet.next()) {

                return new Student(
                    resultStudentSet.getInt(1),
                    resultStudentSet.getInt(2),
                    resultStudentSet.getString(3),
                    resultStudentSet.getString(4));
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }
        return new Student();
    }

    @Override
    public List<Student> getStudents(String query) {
        var students = new ArrayList<Student>();
        try (Connection connection = getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement();
             ResultSet setStudentsByFullName = statement.executeQuery(
                 query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (setStudentsByFullName.next()) {
                students.add(new Student(
                    setStudentsByFullName.getInt(1),
                    setStudentsByFullName.getInt(2),
                    setStudentsByFullName.getString(3),
                    setStudentsByFullName.getString(4)));
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }

        return students;
    }

    @Override
    public Course getCourse(String query) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                 query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (resultSet.next()) {
                return new Course(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }

        return new Course();
    }

    @Override
    public ArrayList<Course> getCourses(String query) {
        var allCoursesList = new ArrayList<Course>();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (resultSet.next()) {
                allCoursesList.add(
                    new Course(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }

        return allCoursesList;
    }

    @Override
    public Group getGroup(String query) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement();
             ResultSet groupSet = statement.executeQuery(
                 query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            if (groupSet.next()) {
                return new Group(groupSet.getInt(1), groupSet.getString(2));
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }

        return new Group();
    }

    @Override
    public List<Group> getGroups(String query) {
        var groups = new ArrayList<Group>();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
             Statement statement = connection.createStatement();
             ResultSet allGroupsSet = statement.executeQuery(query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (allGroupsSet.next()) {
                groups.add(new Group(allGroupsSet.getInt(1), allGroupsSet.getString(2)));
            }

        } catch (Exception e) {
            log.error(ERROR_MESSAGE + query, e);
        }

        return groups;
    }
}
