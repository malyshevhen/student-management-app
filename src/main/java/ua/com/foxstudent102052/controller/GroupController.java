package ua.com.foxstudent102052.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    public List<GroupDto> getAllGroups() throws NoSuchElementException, ElementAlreadyExistException {
        return groupService.getGroups().stream()
            .map(this::setStudentsToGroups)
            .toList();
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws NoSuchElementException, ElementAlreadyExistException {
        return groupService.getGroupsLessThen(numberOfStudents).stream()
            .map(this::setStudentsToGroups)
            .toList();
    }

    private GroupDto setStudentsToGroups(GroupDto groupDto) {
        try {
            var result = groupService.getStudentsByGroup(groupDto.getId());
            groupDto.setStudentList(result);

            return groupDto;
        } catch (NoSuchElementException | DAOException e) {
            return groupDto;
        }

    }
}
