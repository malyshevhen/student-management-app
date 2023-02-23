package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.GroupRepository;
import ua.com.foxstudent102052.model.entity.Group;

class GroupDaoTest extends AbstractTestContainerIT {

    @Autowired
    private GroupRepository groupDao;

    @Test
    void MethodAddGroup_ShouldAddGroupToDb() {
        // given
        var group = Group.builder()
            .name("New Group")
            .build();
        groupDao.save(group);

        // when
        var allGroups = groupDao.findAll();

        // then
        assertEquals(4, allGroups.size());
    }

    @Test
    void MethodGetGroups_ShouldReturnAllGroupsFromDb() {
        // when
        var allGroups = groupDao.findAll();

        // then
        assertEquals(3, allGroups.size());
    }

    @Test
    void MethodGetGroup_ById_ShouldReturnGroupById() {
        // given
        var expected = Group.builder()
            .id(1L)
            .name("Group 1")
            .build();

        // when
        var actual = groupDao.findById(1L).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroup_ByName_ShouldReturnGroupByName() {
        // given
        var expected = Group.builder()
            .id(1L)
            .name("Group 1")
            .build();

        // when
        var actual = groupDao.findByName("Group 1").get();

        // then
        assertEquals(expected, actual);
    }
}
