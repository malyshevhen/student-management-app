package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class GroupRepositoryImpl implements GroupRepository {
    private final DAOFactory daoFactory;

    public GroupRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }


    @Override
    public void addGroup(Group group) {
        try {
            daoFactory.doPost(String.format("""
                    INSERT
                    INTO groups (group_name) values ('%s');""",
                group.getGroupName()));
            log.info("Group: '{}' added successfully", group.getGroupName());
        } catch (SQLException e) {
            var msg = String.format("Error while adding group: '%s'", group.getGroupName());
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public Group getGroupById(int groupId) {
        try {
            String query = String.format("""
                    SELECT *
                    FROM groups
                    WHERE group_id = '%d';""",
                groupId);

            return daoFactory.getGroup(query);
        } catch (SQLException e) {
            var msg = String.format("Error while getting group by id: '%d'", groupId);
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public Group getGroupByName(String groupName) {
        try {
            String query = String.format("""
                    SELECT *
                    FROM groups
                    WHERE group_name = '%s';""",
                groupName);

            return daoFactory.getGroup(query);
        } catch (SQLException e) {
            var msg = String.format("Error while getting group by name: '%s'", groupName);
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public List<Group> getAllGroups() {
        try {
            String query = "SELECT * FROM groups;";

            return daoFactory.getGroups(query);
        } catch (SQLException e) {
            var msg = "Error while getting all groups";
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public List<Group> getGroupsSmallerThen(int numberOfStudents) {
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
        } catch (SQLException e) {
            var msg = String.format("Error while getting groups smaller then: '%d'", numberOfStudents);
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) {
        try {
            String query = String.format("""
                    SELECT *
                    FROM students
                    WHERE group_id = %d;""",
                groupId);

            return daoFactory.getStudents(query);
        } catch (SQLException e) {
            var msg = String.format("Error while getting students by group: '%d'", groupId);
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }
}
