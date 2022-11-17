package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class GroupController {
    private final GroupService groupService;
    
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<GroupDto> getAllGroups() throws NoSuchElementException {
        try {
            var allGroupsDto = groupService.getAllGroups();
            allGroupsDto.forEach(groupDto -> groupDto.setStudentList(getStudentList(groupDto.getId())));

            return allGroupsDto;
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws NoSuchElementException {
        try {
            var unpopularGroupsDto = groupService.getGroupsSmallerThen(numberOfStudents);
            unpopularGroupsDto.forEach(groupDto -> groupDto.setStudentList(getStudentList(groupDto.getId())));

            return unpopularGroupsDto;
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }
    
    private List<StudentDto> getStudentList(int groupId) throws NoSuchElementException {
        try {
            return groupService.getStudentsByGroup(groupId);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            
            throw new NoSuchElementException(e);
        }
    }
}
