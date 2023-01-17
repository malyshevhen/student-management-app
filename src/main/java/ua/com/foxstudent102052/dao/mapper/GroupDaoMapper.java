package ua.com.foxstudent102052.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxstudent102052.model.entity.Group;

public class GroupDaoMapper {
    private GroupDaoMapper() {
    }

    public static Group mapToGroup(ResultSet groupResultSet) throws SQLException {
        return Group.builder()
                .id(groupResultSet.getInt(1))
                .name(groupResultSet.getString(2))
                .build();
    }

    public static List<Group> mapToGroups(ResultSet groupResultSet) throws SQLException {
        var groups = new ArrayList<Group>();

        while (groupResultSet.next()) {
            groups.add(mapToGroup(groupResultSet));
        }

        return groups;
    }
}
