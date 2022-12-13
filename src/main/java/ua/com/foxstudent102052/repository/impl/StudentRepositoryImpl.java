package ua.com.foxstudent102052.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.repository.interfaces.StudentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {
    private final CustomDataSource dataSource;

    public StudentRepositoryImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addStudent(Student student) throws RepositoryException {
        var query =
            """
                INSERT INTO students (
                    group_id,
                    first_name,
                    last_name)
                VALUES (?, ?, ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.groupId());
            statement.setString(2, student.firstName());
            statement.setString(3, student.lastName());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while adding student to the database", e);

            throw new RepositoryException("Error while adding student to the database", e);
        }
    }

    @Override
    public void removeStudent(int studentId) throws RepositoryException {
        var query =
            """
                DELETE FROM students_courses
                WHERE student_id = ?;
                DELETE
                FROM students
                WHERE student_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Student with studentId {} was not removed from the database", studentId);

            throw new RepositoryException(String.format("Student with studentId %d was not removed from the database", studentId), e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws RepositoryException {
        var query =
            """
                INSERT INTO students_courses (
                    student_id,
                    course_id)
                VALUES (?, ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Student with id {} was not added to Course with id {}", studentId, courseId);

            throw new RepositoryException(String.format("Student with id %d was not added to Course with id %d",
                studentId, courseId), e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws RepositoryException {
        var query =
            """
                DELETE
                FROM students_courses
                WHERE student_id = ?
                AND course_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Student with id {} was not removed from course with id {}", studentId, courseId);

            throw new RepositoryException(String.format("Student with id %d was not removed from course with id %d",
                studentId, courseId), e);
        }
    }

    @Override
    public List<Student> getAllStudents() throws RepositoryException {
        var query = "SELECT * FROM students;";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var studentsResultSet = statement.executeQuery(query)) {

            return getStudentsFromResultSet(studentsResultSet);
        } catch (SQLException e) {
            log.error("There are no students in the database");

            throw new RepositoryException("There are no students in the database", e);
        }
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM students
                WHERE student_id IN (
                    SELECT student_id
                    FROM students_courses
                    WHERE course_id = ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            var studentsResultSet = statement.executeQuery();

            return getStudentsFromResultSet(studentsResultSet);
        } catch (SQLException e) {
            log.error("Error while getting students by course id");

            throw new RepositoryException("Error while getting students by course id", e);
        }
    }

    @Override
    public List<Student> getStudentsByNameAndCourse(String studentName, Integer courseId) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM students
                WHERE student_id IN (
                    SELECT student_id
                    FROM students_courses
                    WHERE course_id = ?)
                AND first_name = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.setString(2, studentName);
            var studentsResultSet = statement.executeQuery();

            return getStudentsFromResultSet(studentsResultSet);
        } catch (SQLException e) {
            log.error("Error while getting students by name and course");

            throw new RepositoryException("Error while getting students by name and course", e);
        }
    }

    @Override
    public Student getStudentById(int id) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM students
                WHERE student_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            var studentsResultSet = statement.executeQuery();

            if (studentsResultSet.next()) {
                return getStudentFromResultSet(studentsResultSet);
            } else {
                throw new RepositoryException("No such element in DB");
            }
        } catch (SQLException e) {
            log.error("Error while getting student by id");

            throw new RepositoryException("Error while getting student by id", e);
        }
    }

    @Override
    public Student getLastStudent() throws RepositoryException {
        var query = """
            SELECT *
            FROM students
            WHERE student_id =
                (SELECT MAX(student_id)
                FROM students);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            var studentsResultSet = statement.executeQuery();

            if (studentsResultSet.next()) {
                return getStudentFromResultSet(studentsResultSet);
            } else {
                throw new RepositoryException("No such element in DB");
            }
        } catch (SQLException e) {
            log.error("Error while getting last student");

            throw new RepositoryException("Error while getting last student", e);
        }
    }

    private static Student getStudentFromResultSet(ResultSet studentsResultSet) throws SQLException {
        return Student.builder()
            .studentId(studentsResultSet.getInt(1))
            .groupId(studentsResultSet.getInt(2))
            .firstName(studentsResultSet.getString(3))
            .lastName(studentsResultSet.getString(4))
            .build();
    }

    private static List<Student> getStudentsFromResultSet(ResultSet studentsResultSet) throws SQLException {
        var students = new ArrayList<Student>();

        while (studentsResultSet.next()) {
            students.add(getStudentFromResultSet(studentsResultSet));
        }
        return students;
    }
}
