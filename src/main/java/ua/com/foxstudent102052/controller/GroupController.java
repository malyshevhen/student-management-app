package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.exception.NoSuchGroupExistsException;
import ua.com.foxstudent102052.service.exception.NoSuchStudentExistsException;

import java.util.List;

@Slf4j
public class GroupController {
    private GroupService groupService;
    
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<GroupDto> getAllGroups() {
        try {
            var allGroupsDto = groupService.getAllGroups();
            allGroupsDto.forEach(groupDto -> groupDto.setStudentList(getStudentList(groupDto.getId())));

            return allGroupsDto;
        } catch (NoSuchGroupExistsException e) {
            log.info(e.getMessage());
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) {
        try {
            var unpopularGroupsDto = groupService.getGroupsSmallerThen(numberOfStudents);
            unpopularGroupsDto.forEach(groupDto -> groupDto.setStudentList(getStudentList(groupDto.getId())));

            return unpopularGroupsDto;
        } catch (NoSuchGroupExistsException e) {
            log.info(e.getMessage());
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }

        return List.of();
    }
    
    private List<StudentDto> getStudentList(int groupId) {
        try {
            return groupService.getStudentsByGroup(groupId);
        } catch (NoSuchStudentExistsException e) {
            log.error(e.getMessage());
        }
        
        return List.of();
    }
}
