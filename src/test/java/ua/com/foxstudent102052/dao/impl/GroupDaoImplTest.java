package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.entity.Group;

@JdbcTest
@Testcontainers
@ActiveProfiles("test-containers-flyway")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql({ "/scripts/ddl/Table_creation.sql", "/scripts/dml/testDB_Data.sql" })
class GroupDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GroupDao groupDao;

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(jdbcTemplate);
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
