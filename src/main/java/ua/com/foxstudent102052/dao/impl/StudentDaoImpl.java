package ua.com.foxstudent102052.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.dao.mapper.StudentDaoMapper;
import ua.com.foxstudent102052.model.entity.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class StudentDaoImpl implements StudentDao {
    private static final String TABLE_NAME = "students";
    private static final String TABLE_COLUMN_1 = "student_id";
    private static final String TABLE_COLUMN_2 = "group_id";
    private static final String TABLE_COLUMN_3 = "first_name";
    private static final String TABLE_COLUMN_4 = "last_name";
    private static final String JOIN_TABLE_NAME = "students_courses";
    private static final String JOIN_TABLE_COLUMN_1 = "student_id";
    private static final String JOIN_TABLE_COLUMN_2 = "course_id";


    private final CustomDataSource dataSource;

    public StudentDaoImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addStudent(Student student) {
        var query =
            """
                INSERT INTO %table_name (
                    %table_column2,
                    %table_column3,
                    %table_column4)
                VALUES (?, ?, ?);""";

        query = query.replace("%table_name", TABLE_NAME)
            .replace("%table_column2", TABLE_COLUMN_2)
            .replace("%table_column3", TABLE_COLUMN_3)
            .replace("%table_column4", TABLE_COLUMN_4);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public void removeStudent(int studentId) {
        var query =
            """
                DELETE FROM %join_table_name
                WHERE %join_column1 = ?;
                DELETE
                FROM %table_name
                WHERE %join_column1 = ?;""";

        query = query.replace("%table_name", TABLE_NAME)
            .replace("%join_table_name", JOIN_TABLE_NAME)
            .replace("%join_column1", JOIN_TABLE_COLUMN_1);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, studentId);

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        var query =
            """
                INSERT INTO %join_table_name (
                    %join_column1,
                    %join_column2)
                VALUES (?, ?);""";

        query = query.replace("%join_table_name", JOIN_TABLE_NAME)
            .replace("%join_column1", JOIN_TABLE_COLUMN_1)
            .replace("%join_column2", JOIN_TABLE_COLUMN_2);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Student with id {} was not added to Course with id {}", studentId, courseId);

            throw new DAOException(String.format("Student with id %d was not added to Course with id %d",
                studentId, courseId), e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        var query =
            """
                DELETE
                FROM %join_table_name
                WHERE %join_column1 = ?
                AND %join_column2 = ?;""";

        query = query.replace("%join_table_name", JOIN_TABLE_NAME)
            .replace("%join_column1", JOIN_TABLE_COLUMN_1)
            .replace("%join_column2", JOIN_TABLE_COLUMN_2);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Student> getStudent(int id) {
        var query =
            """
                SELECT *
                FROM %table_name
                WHERE %table_column1 = ?;""";

        query = query.replace("%table_name", TABLE_NAME)
            .replace("%table_column1", TABLE_COLUMN_1);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            var studentsResultSet = statement.executeQuery();

            if (studentsResultSet.next()) {
                var student = StudentDaoMapper.mapToStudent(studentsResultSet);

                return Optional.of(student);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Student> getStudents() {
        var query = "SELECT * FROM %table_name;";

        query = query.replace("%table_name", TABLE_NAME);

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var studentsResultSet = statement.executeQuery(query)) {

            return StudentDaoMapper.mapToStudents(studentsResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Student> getStudents(int courseId) {
        var query =
            """
                SELECT *
                FROM %table_name
                WHERE %table_column1 IN (
                    SELECT %join_column1
                    FROM %join_table_name
                    WHERE %join_column2 = ?);""";

        query = query.replace("%table_name", TABLE_NAME)
            .replace("%table_column1", TABLE_COLUMN_1)
            .replace("%join_table_name", JOIN_TABLE_NAME)
            .replace("%join_column1", JOIN_TABLE_COLUMN_1)
            .replace("%join_column2", JOIN_TABLE_COLUMN_2);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            var studentsResultSet = statement.executeQuery();

            return StudentDaoMapper.mapToStudents(studentsResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Student> getStudents(String studentName, Integer courseId) {
        var query =
            """
                SELECT *
                FROM %table_name
                WHERE %table_column1 IN (
                    SELECT %join_column1
                    FROM %join_table_name
                    WHERE %join_column2 = ?)
                AND %table_column3 = ?;""";

        query = query.replace("%table_name", TABLE_NAME)
            .replace("%table_column1", TABLE_COLUMN_1)
            .replace("%table_column3", TABLE_COLUMN_3)
            .replace("%join_table_name", JOIN_TABLE_NAME)
            .replace("%join_column1", JOIN_TABLE_COLUMN_1)
            .replace("%join_column2", JOIN_TABLE_COLUMN_2);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.setString(2, studentName);

            var studentsResultSet = statement.executeQuery();

            return StudentDaoMapper.mapToStudents(studentsResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }
}
