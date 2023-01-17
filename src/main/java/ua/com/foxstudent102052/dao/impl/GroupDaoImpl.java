package ua.com.foxstudent102052.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.mapper.GroupRowMapper;
import ua.com.foxstudent102052.model.entity.Group;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupDaoImpl implements GroupDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGroup(Group group) {
        var query = """
            INSERT
            INTO groups (group_name)
                values (?);""";

        jdbcTemplate.update(query, group.getName());
    }

    @Override
    public Optional<Group> getGroupById(int groupId) {
        var query = """
            SELECT group_id, group_name
            FROM groups
            WHERE group_id = ?;""";

        return jdbcTemplate.query(query, new GroupRowMapper(), groupId)
            .stream()
            .findFirst();
    }

    @Override
    public Optional<Group> getGroupByName(String groupName) {
        var query = """
            SELECT group_id, group_name
            FROM groups
            WHERE group_name = ?;""";

        return jdbcTemplate.query(query, new GroupRowMapper(), groupName)
            .stream()
            .findFirst();
    }

    @Override
    public List<Group> getAll() {
        var query = """
            SELECT group_id, group_name
            FROM groups;""";

        return jdbcTemplate.query(query, new GroupRowMapper());
    }

    @Override
    public List<Group> getGroupsLessThen(int numberOfStudents) {
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

        return jdbcTemplate.query(query, new GroupRowMapper(), numberOfStudents);
    }
}
