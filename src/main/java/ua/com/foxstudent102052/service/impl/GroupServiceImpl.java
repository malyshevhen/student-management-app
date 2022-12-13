package ua.com.foxstudent102052.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.GroupMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.dto.GroupDto;
import ua.com.foxstudent102052.dto.StudentDto;
import ua.com.foxstudent102052.repository.interfaces.GroupRepository;
import ua.com.foxstudent102052.repository.exceptions.RepositoryException;
import ua.com.foxstudent102052.service.exceptions.ServiceException;
import ua.com.foxstudent102052.service.interfaces.GroupService;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final String GROUP_WITH_ID_NOT_EXIST = "Group with id %d doesn't exist";
    private static final String GROUP_WITH_ID_EXISTS = "Group with id %d already exists";

    private final GroupRepository groupRepository;

    @Override
    public void addGroup(GroupDto groupDto) throws ServiceException {
        try {
            groupRepository.addGroup(GroupMapper.toGroup(groupDto));
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<GroupDto> getGroups() throws ServiceException {
        try {
            var allGroupsList = groupRepository.getAllGroups()
                .stream()
                .map(GroupMapper::toGroupDto)
                .toList();

            log.info("All groups were received");

            return allGroupsList;
        } catch (RepositoryException e) {
            log.error("There are no groups in the database");

            throw new ServiceException("There are no groups in the database", e);
        }
    }

    @Override
    public GroupDto getGroup(int groupId) throws ServiceException {
        try {
            var groupDto = GroupMapper.toGroupDto(groupRepository.getGroup(groupId));

            log.info("Group with id {} was received", groupId);

            return groupDto;
        } catch (RepositoryException e) {
            log.error("Group with id {} doesn't exist", groupId);

            throw new ServiceException(String.format(GROUP_WITH_ID_NOT_EXIST, groupId), e);
        }
    }

    @Override
    public List<GroupDto> getGroupsLessThen(int numberOfStudents) throws ServiceException {
        try {
            var groupList = groupRepository.getGroupsLessThen(numberOfStudents)
                .stream()
                .map(GroupMapper::toGroupDto)
                .toList();

            log.info("Groups with number of students less then {} were received", numberOfStudents);

            return groupList;
        } catch (RepositoryException e) {
            log.error("There are no groups with number of students less then {}", numberOfStudents);

            throw new ServiceException(String.format("There are no groups with number of students less then %d",
                numberOfStudents), e);
        }
    }

    @Override
    public GroupDto getGroup(String groupName) throws ServiceException {
        try {
            var groupDto = GroupMapper.toGroupDto(groupRepository.getGroup(groupName));

            log.info("Group with name {} was received", groupName);

            return groupDto;
        } catch (RepositoryException e) {
            log.error("Group with name {} doesn't exist", groupName);

            throw new ServiceException(String.format("Group with name %s doesn't exist", groupName), e);
        }
    }

    @Override
    public List<StudentDto> getStudentsByGroup(int groupId) throws ServiceException {

        try {
            var studentDtoList = groupRepository.getStudents(groupId)
                .stream()
                .map(StudentMapper::toStudentDto)
                .toList();

            log.info("Students from group with id {} were received", groupId);

            return studentDtoList;
        } catch (RepositoryException e) {
            log.error("There are no students in group with id {}", groupId);

            throw new ServiceException(String.format("There are no students in group with id %d", groupId), e);
        }
    }

    @Override
    public Boolean ifExist(String groupName) {
        try {
            groupRepository.getGroup(groupName);

            return true;
        } catch (RepositoryException e) {
            log.info(e.getMessage(), e);

            return false;
        }
    }
}
