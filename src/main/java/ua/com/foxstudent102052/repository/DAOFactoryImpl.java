package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class DAOFactoryImpl implements DAOFactory {

    private static DAOFactoryImpl instance;

    private DAOFactoryImpl() {
    }

    public static DAOFactoryImpl getInstance() {
        if (instance == null) {
            instance = new DAOFactoryImpl();
        }
        return instance;
    }

    private static final String QUERY_EXEC_SUCCESSFUL = "Query: '{}' executed successfully";
    private static final String CONNECTION_SUCCESSFUL = "Connection to DB successful!";
    private static final String ERROR_MESSAGE = "Error while executing query: {}";

    private String password = "314159";
    private String login = "postgres";
    private String jdbcUrl = "jdbc:postgresql://localhost:5433/students_db";

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
    public void doPost(String query) throws DAOException {

        try (Connection connection = getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement()) {

            log.info(CONNECTION_SUCCESSFUL);
            statement.executeUpdate(query);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public Student getStudent(String query) throws DAOException {
        try (Connection connection = getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement();
                ResultSet resultStudentSet = statement.executeQuery(query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            if (resultStudentSet.next()) {

                return Student.builder()
                        .studentId(resultStudentSet.getInt(1))
                        .groupId(resultStudentSet.getInt(2))
                        .firstName(resultStudentSet.getString(3))
                        .lastName(resultStudentSet.getString(4))
                        .build();
            } else {
                throw new DAOException("No student found");
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public List<Student> getStudents(String query) throws DAOException {
        var students = new ArrayList<Student>();
        try (Connection connection = getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement();
                ResultSet setStudentsByFullName = statement.executeQuery(
                        query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (setStudentsByFullName.next()) {
                students.add(
                        Student.builder()
                                .studentId(setStudentsByFullName.getInt(1))
                                .groupId(setStudentsByFullName.getInt(2))
                                .firstName(setStudentsByFullName.getString(3))
                                .lastName(setStudentsByFullName.getString(4))
                                .build());
            }
            if (students.isEmpty()) {
                throw new DAOException("No students found");
            } else {
                return students;
            }
        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public Course getCourse(String query) throws DAOException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            if (resultSet.next()) {
                return Course.builder()
                        .courseId(resultSet.getInt(1))
                        .courseName(resultSet.getString(2))
                        .courseDescription(resultSet.getString(3))
                        .build();
            } else {
                throw new DAOException("No such course");
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public ArrayList<Course> getCourses(String query) throws DAOException {
        var allCoursesList = new ArrayList<Course>();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (resultSet.next()) {
                allCoursesList.add(
                        Course.builder()
                                .courseId(resultSet.getInt(1))
                                .courseName(resultSet.getString(2))
                                .courseDescription(resultSet.getString(3))
                                .build());
            }
            if (allCoursesList.isEmpty()) {
                throw new DAOException("No courses");
            } else {
                return allCoursesList;
            }
        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public Group getGroup(String query) throws DAOException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement();
                ResultSet groupSet = statement.executeQuery(
                        query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            if (groupSet.next()) {
                return Group.builder()
                        .groupId(groupSet.getInt(1))
                        .groupName(groupSet.getString(2))
                        .build();
            } else {
                throw new DAOException("No such group");
            }

        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public List<Group> getGroups(String query) throws DAOException {
        var groups = new ArrayList<Group>();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement();
                ResultSet allGroupsSet = statement.executeQuery(query)) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            while (allGroupsSet.next()) {
                groups.add(
                        Group.builder()
                                .groupId(allGroupsSet.getInt(1))
                                .groupName(allGroupsSet.getString(2))
                                .build());
            }

            if (groups.isEmpty()) {
                throw new DAOException("No groups");
            } else {
                return groups;
            }
        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);

            throw new DAOException(e);
        }
    }

    @Override
    public void executeSqlScript(String filePath) throws IOException {
        var query = readScriptFromFile(filePath);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, login, password);
                Statement statement = connection.createStatement()) {
            log.info(CONNECTION_SUCCESSFUL);
            log.info(QUERY_EXEC_SUCCESSFUL, query);

            statement.execute(query);
        } catch (SQLException e) {
            log.error(ERROR_MESSAGE + query, e);
        }

    }

    private String readScriptFromFile(String filePath) throws IOException {
        var file = new File(filePath);
        var fileReader = new FileReader(file);

        try (var bufferedReader = new BufferedReader(fileReader)) {
            var stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line)
                        .append("\n");
            }

            return stringBuilder.toString();
        }
    }
}
