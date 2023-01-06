package ua.com.foxstudent102052.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.dao.datasource.impl.PooledDataSource;
import ua.com.foxstudent102052.dao.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class GroupDaoImplTest {
    private CustomDataSource customDataSource;
    private GroupDao groupDao;
    private PostDAO postDAO;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        customDataSource = PooledDataSource.getInstance();
        groupDao = new GroupDaoImpl(customDataSource);
        postDAO = new PostDAOImpl(customDataSource);
        var ddlScript = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        var dmlScript = FileUtils.readTextFile("src/test/resources/scripts/dml/testDB_Data.sql");
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
    void MethodAddGroup_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        groupDao = new GroupDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> groupDao.addGroup(Group.builder().build()),
            "Group wasn`t added");
    }

    @Test
    void MethodGetGroups_ShouldReturnAllGroupsFromDb() throws DAOException {
        // when
        var allGroups = groupDao.getGroups();

        // then
        assertEquals(3, allGroups.size());
    }

    @Test
    void MethodGetGroups_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        groupDao = new GroupDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> groupDao.getGroups(),
            "Groups weren`t received");
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
    void MethodGetGroup_ById_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        groupDao = new GroupDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> groupDao.getGroup(1),
            "Group wasn`t received");
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
    void MethodGetGroup_ByName_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        groupDao = new GroupDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> groupDao.getGroup("Group 1"),
            "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnGroupLessCount() throws DAOException {
        var actual = groupDao.getGroupsLessThen(2);

        assertEquals(1, actual.size());

    }

    @Test
    void MethodGetGroupsLessThen_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {

        // given
        customDataSource = mock(CustomDataSource.class);
        groupDao = new GroupDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> groupDao.getGroupsLessThen(2),
            "Groups weren`t received");
    }

    @Test
    void MethodGetStudents_ByGroup_ShouldReturnListOfStudentsRelatedToGivenGroup() throws DAOException {
        var actual = groupDao.getStudents(2);

        assertEquals(5, actual.size());
    }

    @Test
    void MethodGetStudents_ByGroupShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(CustomDataSource.class);
        groupDao = new GroupDaoImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(DAOException.class, () -> groupDao.getStudents(1),
            "Students weren`t received");
    }
}
