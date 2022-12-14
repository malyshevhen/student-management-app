package ua.com.foxstudent102052.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.repository.interfaces.GroupRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GroupRepositoryImpl implements GroupRepository {
    private final CustomDataSource dataSource;

    public GroupRepositoryImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addGroup(Group group) throws RepositoryException {
        var query =
            """
                INSERT
                INTO groups (group_name)
                    values (?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setString(1, group.groupName());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while adding group: {}", group.groupName(), e);

            throw new RepositoryException(String.format("Error while adding group: '%s'", group.groupName()), e);
        }
    }

    @Override
    public Group getGroup(int groupId) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM groups
                WHERE group_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            var groupResultSet = statement.executeQuery();

            if (groupResultSet.next()) {
                return getGroupFromResultSet(groupResultSet);
            } else {
                throw new RepositoryException("No such group in DB");
            }

        } catch (SQLException e) {

            log.error("Error while getting group by id: {}", groupId, e);

            throw new RepositoryException(String.format("Error while getting group by id: '%d'", groupId), e);
        }
    }

    @Override
    public Group getGroup(String groupName) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM groups
                WHERE group_name = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setString(1, groupName);
            var groupResultSet = statement.executeQuery();

            if (groupResultSet.next()) {
                return getGroupFromResultSet(groupResultSet);
            } else {
                throw new RepositoryException("No such group in DB");
            }
        } catch (SQLException e) {
            log.error("Error while getting group by name: {}", groupName, e);

            throw new RepositoryException("Error while getting group by name", e);
        }
    }

    @Override
    public List<Group> getAllGroups() throws RepositoryException {
        var query = "SELECT * FROM groups;";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var groupResultSet = statement.executeQuery(query);

            return getGroupsFromResultSet(groupResultSet);
        } catch (SQLException e) {
            var msg = "Error while getting all groups";

            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Group> getGroupsLessThen(int numberOfStudents) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM groups
                WHERE group_id
                NOT IN(
                    SELECT group_id
                    FROM students
                    WHERE group_id
                    IS NOT NULL)
                OR group_id IN(
                    SELECT group_id
                    FROM students
                    WHERE group_id
                    IS NOT NULL
                    GROUP BY group_id
                    HAVING COUNT(group_id) <= ?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, numberOfStudents);
            var groupResultSet = statement.executeQuery();

            return getGroupsFromResultSet(groupResultSet);
        } catch (SQLException e) {
            log.error("Error while getting groups smaller then: {}", numberOfStudents);

            throw new RepositoryException(String.format("Error while getting groups smaller then: '%d'", numberOfStudents), e);
        }
    }

    @Override
    public List<Student> getStudents(int groupId) throws RepositoryException {
        var query =
            """
                SELECT *
                FROM students
                WHERE group_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            var studentResultSet = statement.executeQuery();

            return getStudentsFromResultSet(studentResultSet);
        } catch (SQLException e) {
            log.error("Error while getting students by group: {}", groupId);

            throw new RepositoryException(String.format("Error while getting students by group: '%d'", groupId), e);
        }
    }

    private static List<Student> getStudentsFromResultSet(ResultSet studentResultSet) throws SQLException {
        var students = new ArrayList<Student>();
        while (studentResultSet.next()) {
            var student = getStudentFromResultSet(studentResultSet);
            students.add(student);
        }

        return students;
    }

    private static List<Group> getGroupsFromResultSet(ResultSet groupResultSet) throws SQLException {
        var groups = new ArrayList<Group>();
        while (groupResultSet.next()) {
            var group = getGroupFromResultSet(groupResultSet);
            groups.add(group);
        }

        return groups;
    }

    private static Student getStudentFromResultSet(ResultSet studentResultSet) throws SQLException {
        return Student.builder()
            .studentId(studentResultSet.getInt(1))
            .groupId(studentResultSet.getInt(2))
            .firstName(studentResultSet.getString(3))
            .lastName(studentResultSet.getString(4))
            .build();
    }

    private static Group getGroupFromResultSet(ResultSet groupResultSet) throws SQLException {
        return Group.builder()
            .groupId(groupResultSet.getInt(1))
            .groupName(groupResultSet.getString(2))
            .build();
    }
}
