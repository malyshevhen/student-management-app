package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.GroupMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.GroupService;

import java.util.List;

@Slf4j
public class GroupController {
    private GroupService groupService;
    
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public void addGroup(String groupDtoName) {
        try {
            groupService.addGroup(new Group(groupDtoName));
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public void updateGroup(GroupDto groupDto) {
        try {
            groupService.updateGroup(groupDto);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public void removeGroup(int groupId) {
        try {
            groupService.removeGroup(groupId);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    public GroupDto getGroupById(int groupId) {
        try {
            GroupDto groupDto = GroupMapper.groupToDto(groupService.getGroupById(groupId));
            groupDto.setStudentList(getStudentList(groupId));

            return groupDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return new GroupDto();
    }

    public List<GroupDto> getAllGroups() {
        try {
            var allGroupsDto = groupService.getAllGroups()
                    .stream()
                    .map(GroupMapper::groupToDto)
                    .toList();
            allGroupsDto.forEach(groupDto -> groupDto.setStudentList(getStudentList(groupDto.getId())));

            return allGroupsDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return List.of();
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) {
        try {
            var unpopularGroupsDto = groupService.getGroupsSmallerThen(numberOfStudents)
                    .stream()
                    .map(GroupMapper::groupToDto)
                    .toList();
            unpopularGroupsDto.forEach(groupDto -> groupDto.setStudentList(getStudentList(groupDto.getId())));

            return unpopularGroupsDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return List.of();
    }

    public GroupDto getGroupByName(String groupName) {
        try {
            GroupDto groupDto = GroupMapper.groupToDto(groupService.getGroupByName(groupName));
            groupDto.setStudentList(getStudentList(groupDto.getId()));

            return groupDto;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        return new GroupDto();
    }
    
    private List<StudentDto> getStudentList(int groupId) {
        try {
            return groupService.getStudentsByGroup(groupId)
                .stream()
                .map(StudentMapper::studentToDto)
                .toList();
        } catch (IllegalArgumentException e) {
            log.info("There ara no students in group " + groupId);
        }
        
        return List.of();
    }
}
