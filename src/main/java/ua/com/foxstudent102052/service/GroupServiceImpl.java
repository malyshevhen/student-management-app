package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.GroupRepository;

import java.util.List;

@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final String GROUP_WITH_ID_NOT_EXIST = "Group with id %d doesn't exist";
    private static final String GROUP_WITH_ID_EXISTS = "Group with id %d already exists";
    private final GroupRepository groupRepository;

    @Override
    public void addGroup(Group group) {

        if (groupRepository.getGroupByName(group.getGroupName()).getGroupId() == 0) {
            groupRepository.addGroup(group);

        } else {
            throw new IllegalArgumentException(String.format(GROUP_WITH_ID_EXISTS, group.getGroupId()));
        }
    }

    @Override
    public void updateGroup(Group group) {

        if (groupRepository.getGroupById(group.getGroupId()).getGroupId() != 0) {
            groupRepository.updateGroupById(group);

        } else {
            throw new IllegalArgumentException(String.format(GROUP_WITH_ID_NOT_EXIST, group.getGroupId()));
        }      
    }

    @Override
    public void removeGroup(int groupId) {

        if (groupRepository.getGroupById(groupId).getGroupId() != 0) {
            groupRepository.removeGroupById(groupId);

        } else {
            throw new IllegalArgumentException(String.format(GROUP_WITH_ID_NOT_EXIST, groupId));
        } 
    }

    @Override
    public List<Group> getAllGroups() {

        if (!groupRepository.getAllGroups().isEmpty()) {
            return groupRepository.getAllGroups();

        } else {
            throw new IllegalArgumentException("There are no groups in the database");
        }
    }

    @Override
    public Group getGroupById(int groupId) {

        if (groupRepository.getGroupById(groupId).getGroupId() != 0) {
            return groupRepository.getGroupById(groupId);

        } else {
            throw new IllegalArgumentException(String.format(GROUP_WITH_ID_NOT_EXIST, groupId));
        }
    }

    @Override
    public List<Group> getGroupsSmallerThen(int numberOfStudents) {

        if (!groupRepository.getGroupsSmallerThen(numberOfStudents).isEmpty()) {
            return groupRepository.getGroupsSmallerThen(numberOfStudents);

        } else {
            throw new IllegalArgumentException(String.format("There are no groups with number of students less then %d", numberOfStudents));
        }
    }

    @Override
    public Group getGroupByName(String groupName) {

        if (groupRepository.getGroupByName(groupName).getGroupId() != 0) {
            return groupRepository.getGroupByName(groupName);

        } else {
            throw new IllegalArgumentException(String.format("Group with name %s doesn't exist", groupName));
        }
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) {

        if (!groupRepository.getStudentsByGroup(groupId).isEmpty()) {
            return groupRepository.getStudentsByGroup(groupId);

        } else {
            throw new IllegalArgumentException(String.format("There are no students in group with id %d", groupId));
        }
    }
}
