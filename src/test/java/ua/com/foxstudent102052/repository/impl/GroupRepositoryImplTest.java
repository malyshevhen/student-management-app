package ua.com.foxstudent102052.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.datasource.impl.H2CustomDataSource;
import ua.com.foxstudent102052.datasource.interfaces.CustomDataSource;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.GroupRepository;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class GroupRepositoryImplTest {
    private CustomDataSource customDataSource;
    private GroupRepository groupRepository;
    private PostDAO postDAO;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        customDataSource = H2CustomDataSource.getInstance();
        groupRepository = new GroupRepositoryImpl(customDataSource);
        postDAO = new PostDAOImpl(customDataSource);
        var ddlScript = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        var dmlScript = FileUtils.readTextFile("src/test/resources/scripts/dml/testDB_Data.sql");
        postDAO.doPost(ddlScript);
        postDAO.doPost(dmlScript);
    }

    @Test
    void MethodAddGroup_ShouldAddGroupToDb() throws RepositoryException {
        // given
        var group = Group.builder().groupName("New Group").build();
        groupRepository.addGroup(group);

        // when
        var allGroups = groupRepository.getAllGroups();

        // then
        assertEquals(4, allGroups.size());
    }

    @Test
    void MethodAddGroup_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        groupRepository = new GroupRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.addGroup(Group.builder().build()),
            "Group wasn`t added");
    }

    @Test
    void MethodGetGroups_ShouldReturnAllGroupsFromDb() throws RepositoryException {
        // when
        var allGroups = groupRepository.getAllGroups();

        // then
        assertEquals(3, allGroups.size());
    }

    @Test
    void MethodGetGroups_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        groupRepository = new GroupRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getAllGroups(),
            "Groups weren`t received");
    }

    @Test
    void MethodGetGroup_ById_ShouldReturnGroupById() throws DAOException, RepositoryException {
        // given
        var expected = Group.builder().groupId(1).groupName("Group 1").build();

        // when
        var actual = groupRepository.getGroup(1);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ById_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        groupRepository = new GroupRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroup(1),
            "Group wasn`t received");
    }

    @Test
    void MethodGetGroup_ByName_ShouldReturnGroupByName() throws DAOException, RepositoryException {
        // given
        var expected = Group.builder().groupId(1).groupName("Group 1").build();

        // when
        var actual = groupRepository.getGroup("Group 1");

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ByName_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        groupRepository = new GroupRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroup("Group 1"),
            "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnGroupLessCount() throws RepositoryException {
        var actual = groupRepository.getGroupsLessThen(2);

        assertEquals(1, actual.size());

    }

    @Test
    void MethodGetGroupsLessThen_ShouldThrowException_WhenDAOExceptionThrown() throws SQLException {

        // given
        customDataSource = mock(H2CustomDataSource.class);
        groupRepository = new GroupRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroupsLessThen(2),
            "Groups weren`t received");
    }

    @Test
    void MethodGetStudents_ByGroup_ShouldReturnListOfStudentsRelatedToGivenGroup() throws RepositoryException {
        var actual = groupRepository.getStudents(2);

        assertEquals(5, actual.size());
    }

    @Test
    void MethodGetStudents_ByGroupShouldThrowException_WhenDAOExceptionThrown() throws SQLException {
        // given
        customDataSource = mock(H2CustomDataSource.class);
        groupRepository = new GroupRepositoryImpl(customDataSource);

        // when
        doThrow(SQLException.class).when(customDataSource).getConnection();

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getStudents(1),
            "Students weren`t received");
    }
}
