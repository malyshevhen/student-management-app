package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.controller.exceptions.ControllerException;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

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
            var allGroupsDto = groupService.getGroups();
            allGroupsDto.forEach(this::setStudentsToGroups);

            return allGroupsDto;
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws ControllerException {
        try {
            var unpopularGroupDtoList = groupService.getGroupsLessThen(numberOfStudents);
            unpopularGroupDtoList.forEach(this::setStudentsToGroups);

            return unpopularGroupDtoList;
        } catch (NoSuchElementException | ServiceException e) {
            log.error(e.getMessage());

            throw new ControllerException(e);
        }
    }

    private void setStudentsToGroups(GroupDto groupDto) {
        try {
            var result = groupService.getStudentsByGroup(groupDto.getId());
            groupDto.setStudentList(result);

        } catch (NoSuchElementException | ServiceException e) {
            log.info("Students were not received", e);
        }
    }
}
