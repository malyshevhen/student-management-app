package ua.com.foxstudent102052.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.mapper.GroupDaoMapper;
import ua.com.foxstudent102052.model.entity.Group;

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
    public void addGroup(Group group) {
        var query =
            """
                INSERT
                INTO groups (group_name)
                    values (?);""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setString(1, group.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Group> getGroup(int groupId) {
        var query =
            """
                SELECT group_id, group_name
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
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Group> getGroup(String groupName) {
        var query =
            """
                SELECT group_id, group_name
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
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Group> getGroups() {
        var query = """
            SELECT group_id, group_name
            FROM groups;""";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var groupResultSet = statement.executeQuery(query);

            return GroupDaoMapper.mapToGroups(groupResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }

    @Override
    public List<Group> getGroupsLessThen(int numberOfStudents) {
        var query =
            """
                SELECT group_id, group_name
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
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }
}
