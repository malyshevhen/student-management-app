package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.ServiceException;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<GroupDto> getAllGroups() throws ControllerException {
        try {
            var allGroupsDto = groupService.getAllGroups();
            allGroupsDto.forEach(groupDto -> {
                try {
                    groupDto.setStudentList(getStudentList(groupDto.getId()));
                } catch (ControllerException e) {
                    log.error("Students were not received", e);

                    throw new IllegalArgumentException(e);
                }
            });

            return allGroupsDto;
        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new NoSuchElementException(e);
        }
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws ControllerException {
        try {
            var unpopularGroupsDto = groupService.getGroupsSmallerThen(numberOfStudents);
            unpopularGroupsDto.forEach(groupDto -> {
                try {
                    groupDto.setStudentList(getStudentList(groupDto.getId()));
                } catch (ControllerException e) {
                    log.error("Students were not received", e);

                    throw new IllegalArgumentException(e);
                }
            });

            return unpopularGroupsDto;
        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new NoSuchElementException(e);
        }
    }

    private List<StudentDto> getStudentList(int groupId) throws ControllerException {
        try {
            return groupService.getStudentsByGroup(groupId);
        } catch (ServiceException e) {
            log.error(e.getMessage());

            throw new NoSuchElementException(e);
        }
    }
}
