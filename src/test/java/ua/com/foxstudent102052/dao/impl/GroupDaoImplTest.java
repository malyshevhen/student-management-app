package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.utils.FileUtils;

@JdbcTest
class GroupDaoImplTest {
    private final GroupDao groupDao;
    private final PostDAO postDAO;
    private final FileUtils fileUtils;

    @Autowired
    public GroupDaoImplTest(DataSource dataSource) {
        this.groupDao = new GroupDaoImpl(dataSource);
        this.postDAO = new PostDaoImpl(dataSource);
        this.fileUtils = new FileUtils();
    }

    @BeforeEach
    public void setUp() throws DAOException {
        var ddlScript = fileUtils.readFileFromResourcesAsString("scripts/ddl/Table_creation.sql");
        var dmlScript = fileUtils.readFileFromResourcesAsString("scripts/dml/testDB_Data.sql");

        postDAO.doPost(ddlScript);
        postDAO.doPost(dmlScript);
    }

    @Test
    void MethodAddGroup_ShouldAddGroupToDb() throws DAOException {
        // given
        var group = Group.builder().name("New Group").build();
        groupDao.addGroup(group);

        // when
        var allGroups = groupDao.getGroups();

        // then
        assertEquals(4, allGroups.size());
    }

    @Test
    void MethodGetGroups_ShouldReturnAllGroupsFromDb() throws DAOException {
        // when
        var allGroups = groupDao.getGroups();

        // then
        assertEquals(3, allGroups.size());
    }

    @Test
    void MethodGetGroup_ById_ShouldReturnGroupById() throws DAOException {
        // given
        var expected = Group.builder().id(1).name("Group 1").build();

        // when
        var actual = groupDao.getGroup(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ByName_ShouldReturnGroupByName() throws DAOException {
        // given
        var expected = Group.builder().id(1).name("Group 1").build();

        // when
        var actual = groupDao.getGroup("Group 1").get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnGroupLessCount() throws DAOException {
        var actual = groupDao.getGroupsLessThen(2);

        assertEquals(1, actual.size());

    }
}
