package ua.com.foxstudent102052.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.dao.mapper.StudentDaoMapper;
import ua.com.foxstudent102052.model.entity.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentDaoImpl implements StudentDao {
    private final CustomDataSource dataSource;


    @Override
    public void addStudent(Student student) {
        var query = """
            INSERT INTO students (
                group_id, first_name, last_name)
            VALUES (?, ?, ?);""";

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
        var query = """
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
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        var query = """
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

            throw new DAOException(String.format("Student with id %d was not added to Course with id %d",
                studentId, courseId), e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        var query = """
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
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Student> getStudent(int id) {
        var query = """
            SELECT student_id, group_id, first_name, last_name
            FROM students
            WHERE student_id = ?;""";

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
        var query = """
            SELECT student_id, group_id, first_name, last_name
            FROM students;""";

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
    public List<Student> getStudentsByCourse(int courseId) {
        var query = """
            SELECT student_id, group_id, first_name, last_name
            FROM students
            WHERE student_id IN (
                SELECT student_id
                FROM students_courses
                WHERE course_id = ?);""";

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
    public List<Student> getStudentsByGroup(int groupId) {
        var query = """
            SELECT student_id, group_id, first_name, last_name
            FROM students
            WHERE group_id = ? ;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            var studentsResultSet = statement.executeQuery();

            return StudentDaoMapper.mapToStudents(studentsResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Student> getStudents(String studentName, int courseId) {
        var query = """
            SELECT student_id, group_id, first_name, last_name
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

            return StudentDaoMapper.mapToStudents(studentsResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }
}
