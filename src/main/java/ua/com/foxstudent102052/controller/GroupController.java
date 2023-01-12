package ua.com.foxstudent102052.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ua.com.foxstudent102052.dao.exceptions.DAOException;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;
import ua.com.foxstudent102052.service.interfaces.StudentService;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController {
    private final GroupService groupService;
    private final StudentService studentService;

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
            var result = studentService.getStudentsByGroup(groupDto.getId());
            groupDto.setStudentList(result);

            return groupDto;
        } catch (NoSuchElementException | DAOException e) {
            return groupDto;
        }

    }
}
