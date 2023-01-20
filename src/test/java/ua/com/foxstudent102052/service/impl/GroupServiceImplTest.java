package ua.com.foxstudent102052.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.service.interfaces.GroupService;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    private final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private GroupDao groupDao;

    private GroupService groupService;

    @BeforeEach
    void setUp() {
        groupService = new GroupServiceImpl(groupDao, modelMapper);
    }

    @Test
    void MethodAddGroup_ShouldPassGroupToRepository() {
        // given
        var group = Group.builder().name("SomeGroup").build();
        var groupDto = modelMapper.map(group, GroupDto.class);

        // when
        groupService.addGroup(groupDto);

        // then
        verify(groupDao).addGroup(group);
    }

    @Test
    void MethodGetAllGroups_ShouldReturnAllGroupsFromDb() {
        // given
        var groups = List.of(
                Group.builder().name("SomeGroup1").build(),
                Group.builder().name("SomeGroup2").build(),
                Group.builder().name("SomeGroup3").build());

        var expected = groups.stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .toList();

        // when
        when(groupDao.getAll()).thenReturn(groups);

        var actual = groupService.getAll();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupById_ShouldReturnGroupFromDb() {
        var optionalGroup = Optional.of(Group.builder().name("SomeGroup").build());
        when(groupDao.getGroupById(1)).thenReturn(optionalGroup);

        var expected = optionalGroup.map(group -> modelMapper.map(group, GroupDto.class)).orElseThrow();

        var actual = groupService.getGroupById(1);

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupByName_ShouldReturnGroupFromDb() {
        var optionalGroup = Optional.of(Group.builder().name("SomeGroup").build());
        when(groupDao.getGroupByName("SomeGroup")).thenReturn(optionalGroup);

        var expected = optionalGroup.map(group -> modelMapper.map(group, GroupDto.class)).orElseThrow();

        var actual = groupService.getGroupByName("SomeGroup");

        assertEquals(expected, actual);
    }

    @Test
    void MethodGetGroupsSmallerThen_ShouldReturnGroupsFromDb() {
        // given
        var groups = List.of(
                Group.builder().name("SomeGroup1").build(),
                Group.builder().name("SomeGroup2").build(),
                Group.builder().name("SomeGroup3").build());

        var expected = groups.stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .toList();

        // when
        when(groupDao.getGroupsLessThen(3)).thenReturn(groups);

        var actual = groupService.getGroupsLessThen(3);

        // then
        assertEquals(expected, actual);
    }
}
