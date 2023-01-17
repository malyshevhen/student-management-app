package ua.com.foxstudent102052.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ua.com.foxstudent102052.model.entity.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {

    @Override
    @Nullable
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Group.builder()
            .id(rs.getInt("group_id"))
            .name(rs.getString("group_name"))
            .build();
    }
}
