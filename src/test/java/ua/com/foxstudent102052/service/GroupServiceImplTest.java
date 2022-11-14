package ua.com.foxstudent102052.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.GroupRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GroupServiceImplTest {
    private static GroupRepository groupRepository;
    private static GroupService groupService;
    
    @BeforeEach
    void setUp() {
        groupRepository = mock(GroupRepository.class);
        groupService = new GroupServiceImpl(groupRepository);
    }

    @DisplayName("Method addGroup should pass Group to Repository")
    @Test
    void addGroup() {
        var group = new Group("TestGroup");
        doNothing().when(groupRepository).addGroup(group);
        when(groupRepository.getGroupByName(group.getGroupName())).thenReturn(new Group());
        
        groupService.addGroup(group);
        
        verify(groupRepository, times(1)).addGroup(group);
    }

    @DisplayName("Method updateGroup should pass Group to Repository")
    @Test
    void updateGroup() {
        var group = new Group(1,"TestGroup");
        doNothing().when(groupRepository).updateGroupById(group);
        when(groupRepository.getGroupById(group.getGroupId())).thenReturn(group);
        
        groupService.updateGroup(group);
        
        verify(groupRepository, times(1)).updateGroupById(group);
    }

    @DisplayName("Method removeGroup should pass Group to Repository")
    @Test
    void removeGroup() {
        var group = new Group(1,"TestGroup");
        doNothing().when(groupRepository).removeGroupById(group.getGroupId());
        when(groupRepository.getGroupById(group.getGroupId())).thenReturn(group);
        
        groupService.removeGroup(group.getGroupId());
        
        verify(groupRepository, times(1)).removeGroupById(group.getGroupId());
    }
    
    @DisplayName("Method getAllGroups should pass Group to Repository")
    @Test
    void getAllGroups() {
        var group = new Group(1,"TestGroup");
        when(groupRepository.getAllGroups()).thenReturn(List.of(group));
        
        groupService.getAllGroups();
        
        verify(groupRepository, times(2)).getAllGroups();
    }

    @DisplayName("Method getGroupById return Group from Repository")
    @Test
    void getGroupById() {
        var expected = new Group(1,"TestGroup");
        when(groupRepository.getGroupById(expected.getGroupId())).thenReturn(expected);

        var actual = groupService.getGroupById(expected.getGroupId());

        assertEquals(expected, actual);
    }

    @DisplayName("Method getGroupByName return list of Groups from Repository")
    @Test
    void getGroupsSmallerThen() {
        var group = new Group(1, "Test Group");
        var expected = List.of(group);
        when(groupRepository.getGroupsSmallerThen(1)).thenReturn(List.of(group));

        List<Group> actual = groupService.getGroupsSmallerThen(1);

        assertEquals(expected, actual);
    }

    @DisplayName("Method getGroupsBiggerThen return Group from Repository")
    @Test
    void getGroupByName() {
        var expected = new Group(1, "Test Group");
        when(groupRepository.getGroupByName(expected.getGroupName())).thenReturn(expected);
        
        var actual = groupService.getGroupByName(expected.getGroupName());
        
        assertEquals(expected, actual);
    }

    @DisplayName("Method getStudentsByGroup return list of Students from Repository")
    @Test
    void getStudentsByGroup() {
        var expected = List.of(new Student(1, 1, "Test First Name", "Test Surname"));
        when(groupRepository.getStudentsByGroup(1)).thenReturn(expected);
        
        var actual = groupService.getStudentsByGroup(1);
        
        assertEquals(expected, actual);
    }
}
