package ua.com.foxstudent102052.controller;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.service.GroupService;
import ua.com.foxstudent102052.service.ServiceException;

import java.util.List;

@Slf4j
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<GroupDto> getAllGroups() throws ControllerException {
        try {
            var allGroupsDto = groupService.getAllGroups();
            allGroupsDto.forEach(this::setStudentsToGroups);
            log.info("All groups were successfully received");

            return allGroupsDto;
        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new ControllerException(e);
        }
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws ControllerException {
        try {
            var unpopularGroupsDto = groupService.getGroupsSmallerThen(numberOfStudents);
            unpopularGroupsDto.forEach(this::setStudentsToGroups);
            log.info("Unpopular groups were found");

            return unpopularGroupsDto;
        } catch (ServiceException e) {
            log.info(e.getMessage());

            throw new ControllerException(e);
        }
    }

    private void setStudentsToGroups(GroupDto groupDto) {
        try {
            var result = groupService.getStudentsByGroup(groupDto.getId());
            groupDto.setStudentList(result);

        } catch (ServiceException e) {
            log.info("Students were not received", e);
        }
    }
}
