package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController {
    private final GroupService groupService;

    public List<GroupDto> getAllGroups()
            throws NoSuchElementException, ElementAlreadyExistException, DataAccessException {
        return groupService.getAll();
    }

    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents)
            throws NoSuchElementException, ElementAlreadyExistException, DataAccessException {
        return groupService.getGroupsLessThen(numberOfStudents);
    }
}
