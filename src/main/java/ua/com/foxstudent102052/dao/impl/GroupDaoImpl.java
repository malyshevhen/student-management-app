package ua.com.foxstudent102052.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.entity.Group;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupDaoImpl implements GroupDao {
    private final JdbcTemplate jdbcTemplate;

    @Qualifier("groupRowMapper")
    private final RowMapper<Group> groupRowMapper;

    @Override
    public void addGroup(Group group) throws DataAccessException {
        var query = """
                INSERT
                INTO groups (group_name)
                    values (?);""";

        jdbcTemplate.update(query, group.getGroupName());
    }

    @Override
    public Optional<Group> getGroupById(int groupId) throws DataAccessException {
        var query = """
                SELECT group_id, group_name
                FROM groups
                WHERE group_id = ?;""";

        return jdbcTemplate.query(query, groupRowMapper, groupId).stream().findFirst();
    }

    @Override
    public Optional<Group> getGroupByName(String groupName) throws DataAccessException {
        var query = """
                SELECT group_id, group_name
                FROM groups
                WHERE group_name = ?;""";

        return jdbcTemplate.query(query, groupRowMapper, groupName).stream().findFirst();
    }

    @Override
    public List<Group> getAll() throws DataAccessException {
        var query = """
                SELECT group_id, group_name
                FROM groups;""";

        return jdbcTemplate.query(query, groupRowMapper);
    }

    @Override
    public List<Group> getGroupsLessThen(int numberOfStudents) throws DataAccessException {
        var query = """
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

        return jdbcTemplate.query(query, groupRowMapper, numberOfStudents);
    }
}
