package ua.com.foxstudent102052.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.repository.GroupRepository;

public class GroupServiceImplTest {
    private GroupRepository groupRepository;
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        groupRepository = mock(GroupRepository.class);
        groupService = new GroupServiceImpl(groupRepository);
    }

    @Test
    void MethodAddGroupShouldPassGroupToRepository() {
        // given
        var group = Group.builder().groupName("SomeGroup").build();
        var groupDto = GroupDto.builder().name("SomeGroup").build();
        // when
    }

    @Test
    void testGetAllGroups() {

    }

    @Test
    void testGetGroupById() {

    }

    @Test
    void testGetGroupByName() {

    }

    @Test
    void testGetGroupsSmallerThen() {

    }

    @Test
    void testGetStudentsByGroup() {

    }
}
