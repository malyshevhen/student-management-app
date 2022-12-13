package ua.com.foxstudent102052.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.datasource.impl.H2CustomDataSource;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exceptions.DAOException;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.repository.interfaces.PostDAO;
import ua.com.foxstudent102052.repository.interfaces.GroupRepository;
import ua.com.foxstudent102052.utils.FileUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class GroupRepositoryImplTest {
    private ua.com.foxstudent102052.repository.interfaces.PostDAO daoFactory;
    private GroupRepository groupRepository;

    @BeforeEach
    public void setUp() throws IOException, DAOException {
        daoFactory = new PostDAOImpl(H2CustomDataSource.getInstance());
        groupRepository = new GroupRepositoryImpl(daoFactory);
        var query = FileUtils.readTextFile("src/test/resources/scripts/ddl/testDB.sql");
        daoFactory.doPost(query);
    }

    @Test
    void MethodAddGroup_ShouldAddGroupToDb() throws RepositoryException {
        var group = Group.builder().groupName("Group 1").build();
        groupRepository.addGroup(group);
        var allGroups = groupRepository.getAllGroups();

        assertEquals(1, allGroups.size());
    }

    @Test
    void MethodAddGroup_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).doPost(anyString());

        assertThrows(RepositoryException.class, () -> groupRepository.addGroup(Group.builder().build()),
                "Group wasn`t added");
    }

    @Test
    void MethodGetGroups_ShouldReturnAllGroupsFromDb() throws RepositoryException {
        var groups = List.of(
                Group.builder().groupId(1).groupName("Group 1").build(),
                Group.builder().groupId(2).groupName("Group 2").build(),
                Group.builder().groupId(3).groupName("Group 3").build());

        groups.forEach(group -> {

            try {
                groupRepository.addGroup(group);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });

        var allGroups = groupRepository.getAllGroups();

        assertEquals(3, allGroups.size());
    }

    @Test
    void MethodGetGroups_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroups(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getAllGroups(),
                "Groups weren`t received");
    }

    @Test
    void MethodGetGroup_ById_ShouldReturnGroupById() throws DAOException, RepositoryException {
        var expected = Group.builder().groupId(1).groupName("Group 1").build();

        daoFactory.doPost(
                """
                        INSERT
                        INTO groups (group_name) values ('Group 1');
                        """);

        var actual = groupRepository.getGroup(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ById_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroup(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroup(1),
                "Group wasn`t received");
    }

    @Test
    void MethodGetGroup_ByName_ShouldReturnGroupByName() throws DAOException, RepositoryException {
        var expected = Group.builder().groupId(1).groupName("Group 1").build();

        daoFactory.doPost(
                """
                        INSERT
                        INTO groups (group_name) values ('Group 1');
                        """);

        var actual = groupRepository.getGroup("Group 1");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ByName_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroup(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroup("Group 1"),
                "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsLessThen_ShouldReturnGroupLessCount() throws RepositoryException {
        var students = List.of(
                Student.builder().groupId(1).firstName("Dart").lastName("Vaider").build(),
                Student.builder().groupId(1).firstName("Luke").lastName("Skywalker").build(),
                Student.builder().groupId(1).firstName("Han").lastName("Solo").build(),
                Student.builder().groupId(2).firstName("Leia").lastName("Organa").build(),
                Student.builder().groupId(2).firstName("Chewbacca").lastName("Wookie").build());

        var groups = List.of(
                Group.builder().groupId(1).groupName("Group 1").build(),
                Group.builder().groupId(2).groupName("Group 2").build(),
                Group.builder().groupId(3).groupName("Group 3").build());

        groups.forEach(group -> {
            try {
                groupRepository.addGroup(group);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });

        students.forEach(student -> {

            try {
                daoFactory.doPost(String.format(
                        """
                                INSERT INTO students (first_name, last_name, group_id)
                                VALUES ('%s', '%s', %d);
                                """,
                        student.firstName(), student.lastName(), student.groupId()));
            } catch (DAOException e) {
                e.printStackTrace();

                fail();
            }
        });

        var actual = groupRepository.getGroupsLessThen(2);

        assertEquals(2, actual.size());

    }

    @Test
    void MethodGetGroupsLessThen_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {

        // given
        daoFactory = mock(PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroups(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroupsLessThen(2),
                "Groups weren`t received");
    }

    @Test
    void MethodGetStudents_ByGroup_ShouldReturnListOfStudentsRelatedToGivenGroup() throws RepositoryException {
        var students = List.of(
                Student.builder().groupId(1).firstName("Dart").lastName("Vaider").build(),
                Student.builder().groupId(1).firstName("Luke").lastName("Skywalker").build(),
                Student.builder().groupId(1).firstName("Han").lastName("Solo").build(),
                Student.builder().groupId(2).firstName("Leia").lastName("Organa").build(),
                Student.builder().groupId(2).firstName("Chewbacca").lastName("Wookie").build());

        var groups = List.of(
                Group.builder().groupId(1).groupName("Group 1").build(),
                Group.builder().groupId(2).groupName("Group 2").build(),
                Group.builder().groupId(3).groupName("Group 3").build());

        groups.forEach(group -> {
            try {
                groupRepository.addGroup(group);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });

        students.forEach(student -> {

            try {
                daoFactory.doPost(String.format(
                        """
                                INSERT INTO students (first_name, last_name, group_id)
                                VALUES ('%s', '%s', %d);
                                """,
                        student.firstName(), student.lastName(), student.groupId()));
            } catch (DAOException e) {
                e.printStackTrace();

                fail();
            }
        });

        var actual = groupRepository.getStudents(1);

        assertEquals(3, actual.size());
    }

    @Test
    void MethodGetStudents_ByGroupShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(ua.com.foxstudent102052.repository.interfaces.PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getStudents(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getStudents(1),
                "Students weren`t received");
    }

    @Test
    void MethodGetLastGroup_ShouldReturnLastGroupFromDB() throws RepositoryException {
        List.of(
                Group.builder().groupName("Group 1").build(),
                Group.builder().groupName("Group 2").build(),
                Group.builder().groupName("Group 3").build(),
                Group.builder().groupName("Group 4").build())
                .forEach(group -> {
                    try {
                        groupRepository.addGroup(group);

                    } catch (RepositoryException e) {
                        fail();
                    }
                });

        var expected = Group.builder().groupId(4).groupName("Group 4").build();
        var actual = groupRepository.getLastGroup();

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetLastGroup_ShouldThrowException_WhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(PostDAO.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroup(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getLastGroup(),
                "Error while getting last group");
    }
}
