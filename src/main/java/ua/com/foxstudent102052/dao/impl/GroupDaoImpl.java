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
    public static final String TABLE_NAME = "groups";
    public static final String TABLE_COLUMN_1 = "group_id";
    public static final String TABLE_COLUMN_2 = "group_name";
    public static final String SECOND_TABLE_NAME = "students";
    public static final String SECOND_TABLE_COLUMN_2 = "group_id";
    private final CustomDataSource dataSource;

    public GroupDaoImpl(CustomDataSource customDataSource) {
        this.dataSource = customDataSource;
    }

    @Override
    public void addGroup(Group group) {
        var query =
            """
                INSERT
                INTO $table_name ($col2)
                    values (?);""";

        query = query.replace("$table_name", TABLE_NAME)
            .replace("$col2", TABLE_COLUMN_2);

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
                SELECT *
                FROM $table_name
                WHERE $table_col1 = ?;""";

        query = query.replace("$table_name", TABLE_NAME)
            .replace("$table_col1", TABLE_COLUMN_1);

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
                SELECT *
                FROM $table_name
                WHERE $col2 = ?;""";

        query = query.replace("$table_name", TABLE_NAME)
            .replace("$col2", TABLE_COLUMN_2);

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
        var query = "SELECT * FROM $table_name;";

        query = query.replace("$table_name", TABLE_NAME);

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
                SELECT *
                FROM %table_name
                WHERE %table_column1
                NOT IN(
                    SELECT %second_table_column2
                    FROM %second_table_name
                    WHERE %second_table_column2
                    IS NOT NULL)
                OR %table_column1 IN(
                    SELECT %second_table_column2
                    FROM %second_table_name
                    WHERE %second_table_column2
                    IS NOT NULL
                    GROUP BY %second_table_column2
                    HAVING COUNT(%second_table_column2) <= ?);""";

        query = query.replace("%table_name", TABLE_NAME)
            .replace("%second_table_name", SECOND_TABLE_NAME)
            .replace("%table_column1", TABLE_COLUMN_1)
            .replace("%second_table_column2", SECOND_TABLE_COLUMN_2);

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

    @Override
    public List<Student> getStudents(int groupId) {
        var query =
            """
                SELECT *
                FROM %second_table_name
                WHERE %second_table_column2 = ?;""";

        query = query.replace("%second_table_name", SECOND_TABLE_NAME)
            .replace("%second_table_column2", SECOND_TABLE_COLUMN_2);

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);

            var studentResultSet = statement.executeQuery();

            return StudentDaoMapper.mapToStudents(studentResultSet);
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new DAOException(e);
        }
    }
}
