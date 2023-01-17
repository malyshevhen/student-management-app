package ua.com.foxstudent102052.dao.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.entity.Group;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest extends AbstractTestContainerIT {

    private final GroupDao groupDao;

    @Autowired
    public GroupDaoImplTest(JdbcTemplate jdbcTemplate) {
        groupDao = new GroupDaoImpl(jdbcTemplate);
    }

    @BeforeAll
    static void setUp() {
        start();
    }

    @AfterAll
    static void tearDown() {
        close();
    }

    @Test
    void MethodAddGroup_ShouldAddGroupToDb() {
        // given
        var group = Group.builder().name("New Group").build();
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
        var expected = Group.builder().id(1).name("Group 1").build();

        // when
        var actual = groupDao.getGroupById(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ByName_ShouldReturnGroupByName() {
        // given
        var expected = Group.builder().id(1).name("Group 1").build();

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
