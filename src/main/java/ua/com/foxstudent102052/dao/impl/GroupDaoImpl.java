package ua.com.foxstudent102052.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.mapper.GroupDaoMapper;
import ua.com.foxstudent102052.dao.mapper.StudentDaoMapper;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GroupDaoImpl implements GroupDao {
    private final CustomDataSource dataSource;

    public GroupDaoImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addGroup(Group group) throws DAOException {
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

            throw new DAOException(String.format("Error while adding group: '%s'", group.groupName()), e);
        }
    }

    @Override
    public Optional<Group> getGroup(int groupId) throws DAOException {
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
                Group group = GroupDaoMapper.mapToGroup(groupResultSet);

                return Optional.ofNullable(group);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {

            log.error("Error while getting group by id: {}", groupId, e);

            throw new DAOException(String.format("Error while getting group by id: '%d'", groupId), e);
        }
    }

    @Override
    public Optional<Group> getGroup(String groupName) throws DAOException {
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
                var group = GroupDaoMapper.mapToGroup(groupResultSet);

                return Optional.ofNullable(group);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error while getting group by name: {}", groupName, e);

            throw new DAOException("Error while getting group by name", e);
        }
    }

    @Override
    public List<Group> getGroups() throws DAOException {
        var query = "SELECT * FROM groups;";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var groupResultSet = statement.executeQuery(query);

            return GroupDaoMapper.mapToGroups(groupResultSet);
        } catch (SQLException e) {
            var msg = "Error while getting all groups";

            log.error(msg, e);

            throw new DAOException(msg, e);
        }
    }

    @Override
    public List<Group> getGroupsLessThen(int numberOfStudents) throws DAOException {
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

            return GroupDaoMapper.mapToGroups(groupResultSet);
        } catch (SQLException e) {
            log.error("Error while getting groups smaller then: {}", numberOfStudents);

            throw new DAOException(String.format("Error while getting groups smaller then: '%d'", numberOfStudents), e);
        }
    }

    @Override
    public List<Student> getStudents(int groupId) throws DAOException {
        var query =
            """
                SELECT *
                FROM students
                WHERE group_id = ?;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            var studentResultSet = statement.executeQuery();

            return StudentDaoMapper.mapToStudents(studentResultSet);
        } catch (SQLException e) {
            log.error("Error while getting students by group: {}", groupId);

            throw new DAOException(String.format("Error while getting students by group: '%d'", groupId), e);
        }
    }
}
