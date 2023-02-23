package ua.com.foxstudent102052.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.service.exceptions.ElementAlreadyExistException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getAllGroups()
            throws NoSuchElementException, ElementAlreadyExistException, DataAccessException {
        return groupService.getAll();
    }


    @GetMapping("/{minCount}")
    public List<GroupDto> getGroupsSmallerThen(@PathVariable Long minCount)
            throws NoSuchElementException, ElementAlreadyExistException, DataAccessException {
        return groupService.getGroupsLessThen(minCount);
    }
}
