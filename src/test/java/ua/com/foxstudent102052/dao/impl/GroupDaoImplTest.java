package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.entity.Group;

class GroupDaoImplTest extends AbstractTestContainerIT {

    private final GroupDao groupDao;

    private RowMapper<Group> groupRowMapper = BeanPropertyRowMapper.newInstance(Group.class);

    @Autowired
    public GroupDaoImplTest(JdbcTemplate jdbcTemplate) {
        groupDao = new GroupDaoImpl(jdbcTemplate, groupRowMapper);
    }

    @Test
    void MethodAddGroup_ShouldAddGroupToDb() {
        // given
        var group = Group.builder().groupName("New Group").build();
        groupDao.addGroup(group);

        // when
        var allGroups = groupDao.getAll();

        // then
        assertEquals(4, allGroups.size());
    }

    @Test
    void MethodGetGroups_ShouldReturnAllGroupsFromDb() {
        // when
        var allGroups = groupDao.getAll();

        // then
        assertEquals(3, allGroups.size());
    }

    @Test
    void MethodGetGroup_ById_ShouldReturnGroupById() {
        // given
        var expected = Group.builder()
                .groupId(1)
                .groupName("Group 1")
                .build();

        // when
        var actual = groupDao.getGroupById(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ByName_ShouldReturnGroupByName() {
        // given
        var expected = Group.builder()
                .groupId(1)
                .groupName("Group 1")
                .build();

        // when
        var actual = groupDao.getGroupByName("Group 1").get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnGroupLessCount() {
        var actual = groupDao.getGroupsLessThen(2);

        assertEquals(1, actual.size());
    }
}
