package ua.com.foxstudent102052.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

@Slf4j
public class GroupRepositoryImpl implements GroupRepository {
    private final DAOFactory daoFactory;

    public GroupRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addGroup(Group group) throws RepositoryException {
        try {
            daoFactory.doPost(String.format("""
                    INSERT
                    INTO groups (group_name) values ('%s');""",
                    group.getGroupName()));
            log.info("Group: '{}' added successfully", group.getGroupName());
        } catch (DAOException e) {
            var msg = String.format("Error while adding group: '%s'", group.getGroupName());
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Group getGroupById(int groupId) throws RepositoryException {
        try {
            String query = String.format("""
                    SELECT *
                    FROM groups
                    WHERE group_id = '%d';""",
                    groupId);

            return daoFactory.getGroup(query);
        } catch (DAOException e) {
            var msg = String.format("Error while getting group by id: '%d'", groupId);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Group getGroupByName(String groupName) throws RepositoryException {
        try {
            String query = String.format("""
                    SELECT *
                    FROM groups
                    WHERE group_name = '%s';""",
                    groupName);

            return daoFactory.getGroup(query);
        } catch (DAOException e) {
            var msg = String.format("Error while getting group by name: '%s'", groupName);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Group> getAllGroups() throws RepositoryException {
        try {
            String query = "SELECT * FROM groups;";

            return daoFactory.getGroups(query);
        } catch (DAOException e) {
            var msg = "Error while getting all groups";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Group> getGroupsSmallerThen(int numberOfStudents) throws RepositoryException {
        try {
            String query = String.format("""
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

            return daoFactory.getGroups(query);
        } catch (DAOException e) {
            var msg = String.format("Error while getting groups smaller then: '%d'", numberOfStudents);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) throws RepositoryException {
        try {
            String query = String.format("""
                    SELECT *
                    FROM students
                    WHERE group_id = %d;""",
                    groupId);

            return daoFactory.getStudents(query);
        } catch (DAOException e) {
            var msg = String.format("Error while getting students by group: '%d'", groupId);
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }
}
