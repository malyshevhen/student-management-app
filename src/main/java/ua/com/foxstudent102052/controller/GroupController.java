package ua.com.foxstudent102052.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.service.interfaces.GroupService;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getAllGroups() {
        return groupService.getAll();
    }

    @PostMapping("/add")
    public void addGroup(@RequestBody GroupDto groupDto) {
        groupService.addGroup(groupDto);
    }

    @GetMapping("/{minCount}")
    public List<GroupDto> getGroupsSmallerThen(@PathVariable Long minCount) {
        return groupService.getGroupsLessThen(minCount);
    }
}
