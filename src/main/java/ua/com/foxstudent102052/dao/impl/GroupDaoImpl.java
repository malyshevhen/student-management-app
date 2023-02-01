package ua.com.foxstudent102052.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.entity.Group;

@Repository
@Transactional
public class GroupDaoImpl implements GroupDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addGroup(Group group) throws DataAccessException {
        entityManager.persist(group);
    }

    @Override
    public Optional<Group> getGroupById(int groupId) throws DataAccessException {
        var group = entityManager.find(Group.class, groupId);

        return Optional.ofNullable(group);
    }

    @Override
    public Optional<Group> getGroupByName(String groupName) throws DataAccessException {
        var query = """
                SELECT g
                FROM Group g
                WHERE g.groupName =: groupName""";

        var group = entityManager.createQuery(query, Group.class).setParameter("groupName", groupName)
                .getSingleResult();

        return Optional.ofNullable(group);
    }

    @Override
    public List<Group> getAll() throws DataAccessException {
        var query = """
                SELECT g
                FROM Group g""";

        return entityManager.createQuery(query, Group.class).getResultList();
    }

    @Override
    public List<Group> getGroupsLessThen(int numberOfStudents) throws DataAccessException {
        return getAll().stream()
                .filter(g -> g.getStudents().size() <= numberOfStudents)
                .toList();
    }
}
