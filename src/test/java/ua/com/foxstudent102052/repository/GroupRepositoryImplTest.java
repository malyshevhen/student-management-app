package ua.com.foxstudent102052.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

class GroupRepositoryImplTest {
    private DAOFactory daoFactory;
    private GroupRepository groupRepository;

    @BeforeEach
    public void setUp() throws IOException {
        daoFactory = DAOFactoryImpl.getInstance();
        groupRepository = new GroupRepositoryImpl(daoFactory);
        daoFactory.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        daoFactory.setLogin("sa");
        daoFactory.setPassword("sa");
        daoFactory.executeSqlScript("src/test/resources/testDB.sql");
    }

    @Test
    void MethodAddGroupShouldAddGroupToDb() throws RepositoryException {
        var group = Group.builder().groupName("Group 1").build();
        groupRepository.addGroup(group);
        var allGroups = groupRepository.getAllGroups();

        assertEquals(1, allGroups.size());
    }

    @Test
    void MethodAddGroupShouldThrowExceptionWhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).doPost(anyString());

        assertThrows(RepositoryException.class, () -> groupRepository.addGroup(new Group()),
            "Group wasn`t added");
    }

    @Test
    void MethodGetAllGroupsShouldReturnAllGroupsFromDb() throws RepositoryException {
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
    void MethodGetAllGroupsShouldThrowExceptionWhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroups(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getAllGroups(),
            "Groups weren`t received");
    }

    @Test
    void MethodGetGroupByIdShouldReturnGroupById() throws DAOException, RepositoryException {
        var expected = Group.builder().groupId(1).groupName("Group 1").build();

        daoFactory.doPost(
            """
                INSERT
                INTO groups (group_name) values ('Group 1');
                """);

        var actual = groupRepository.getGroupById(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByIdShouldThrowExceptionWhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroup(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroupById(1),
            "Group wasn`t received");
    }

    @Test
    void MethodGetGroupByNameShouldReturnGroupByName() throws DAOException, RepositoryException {
        var expected = Group.builder().groupId(1).groupName("Group 1").build();

        daoFactory.doPost(
            """
                INSERT
                INTO groups (group_name) values ('Group 1');
                """);

        var actual = groupRepository.getGroupByName("Group 1");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByNameShouldThrowExceptionWhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroup(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroupByName("Group 1"),
            "Group wasn`t received");
    }

    @Test
    void MethodGetGroupsSmallerThenShouldReturnGroupLessCount() throws RepositoryException {
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
                    student.getFirstName(), student.getLastName(), student.getGroupId()));
            } catch (DAOException e) {
                e.printStackTrace();

                fail();
            }
        });

        var actual = groupRepository.getGroupsSmallerThen(2);

        assertEquals(2, actual.size());

    }

    @Test
    void MethodGetGroupsSmallerThenShouldThrowExceptionWhenDAOExceptionThrown()throws DAOException {

        // given
        daoFactory = mock(DAOFactory.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getGroups(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getGroupsSmallerThen(2),
            "Groups weren`t received");
    }

    @Test
    void MethodGetStudentsByGroupShouldReturnListOfStudentsRelatedToGivenGroup() throws RepositoryException {
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
                    student.getFirstName(), student.getLastName(), student.getGroupId()));
            } catch (DAOException e) {
                e.printStackTrace();

                fail();
            }
        });

        var actual = groupRepository.getStudentsByGroup(1);

        assertEquals(3, actual.size());
    }

    @Test
    void MethodGetStudentsByGroupShouldThrowExceptionWhenDAOExceptionThrown() throws DAOException {
        // given
        daoFactory = mock(DAOFactory.class);
        groupRepository = new GroupRepositoryImpl(daoFactory);

        // when
        doThrow(DAOException.class).when(daoFactory).getStudents(anyString());

        // then
        assertThrows(RepositoryException.class, () -> groupRepository.getStudentsByGroup(1),
            "Students weren`t received");
    }
}
