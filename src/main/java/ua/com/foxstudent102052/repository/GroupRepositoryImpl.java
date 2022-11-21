package ua.com.foxstudent102052.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

@Slf4j
public class GroupRepositoryImpl implements GroupRepository {
    private static final String QUERY_EXECUTED_SUCCESSFULLY = "Query executed successfully";

    private final DAOFactory daoFactory;

    public GroupRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addGroup(Group group) throws RepositoryException {
        var query = String.format("""
                INSERT
                INTO groups (group_name) values ('%s');""",
                group.getGroupName());

        try {
            daoFactory.doPost(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (DAOException e) {
            var msg = String.format("Error while adding group: '%s'", group.getGroupName());
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Group getGroupById(int groupId) throws RepositoryException {
        var query = String.format("""
                SELECT *
                FROM groups
                WHERE group_id = '%d';""",
                groupId);

        try {
            var group = daoFactory.getGroup(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return group;

        } catch (DAOException e) {
            var msg = String.format("Error while getting group by id: '%d'", groupId);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Group getGroupByName(String groupName) throws RepositoryException {
        var query = String.format("""
                SELECT *
                FROM groups
                WHERE group_name = '%s';""",
                groupName);

        try {
            var group = daoFactory.getGroup(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return group;

        } catch (DAOException e) {
            var msg = String.format("Error while getting group by name: '%s'", groupName);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Group> getAllGroups() throws RepositoryException {
        var query = "SELECT * FROM groups;";

        try {
            var groups = daoFactory.getGroups(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return groups;

        } catch (DAOException e) {
            var msg = "Error while getting all groups";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Group> getGroupsSmallerThen(int numberOfStudents) throws RepositoryException {
        var query = String.format("""
                SELECT *
                FROM groups
                WHERE group_id
                NOT IN(
                    SELECT group_id
                    FROM students
                    WHERE group_id
                    IS NOT NULL)
                OR group_id IN(
                    SELECT group_id
                    FROM students
                    WHERE group_id
                    IS NOT NULL
                    GROUP BY group_id
                    HAVING COUNT(group_id) <= %d);""",
                numberOfStudents);

        try {
            var groups = daoFactory.getGroups(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return groups;

        } catch (DAOException e) {
            var msg = String.format("Error while getting groups smaller then: '%d'", numberOfStudents);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) throws RepositoryException {
        var query = String.format("""
                SELECT *
                FROM students
                WHERE group_id = %d;""",
                groupId);

        try {
            var students = daoFactory.getStudents(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return students;

        } catch (DAOException e) {
            var msg = String.format("Error while getting students by group: '%d'", groupId);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Group getLastGroup() throws RepositoryException {
        var query = """
                SELECT *
                FROM groups
                WHERE group_id =
                    (SELECT MAX(group_id)
                    FROM groups);""";

        try {
            var group = daoFactory.getGroup(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return group;

        } catch (Exception e) {
            var msg = "Error while getting last group";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Boolean ifExist(String groupName) throws RepositoryException {
        var query = String.format("""
                SELECT *
                FROM groups
                WHERE group_name = '%s';""",
                groupName);

        try {
            var group = daoFactory.getGroup(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return group != null;

        } catch (DAOException e) {
            var msg = String.format("Error while checking if group exist: '%s'", groupName);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }
}
