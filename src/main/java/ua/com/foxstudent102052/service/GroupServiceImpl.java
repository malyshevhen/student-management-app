package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.GroupMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.GroupRepository;
import ua.com.foxstudent102052.repository.RepositoryException;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final String GROUP_WITH_ID_NOT_EXIST = "Group with id %d doesn't exist";
    private static final String GROUP_WITH_ID_EXISTS = "Group with id %d already exists";
    private final GroupRepository groupRepository;

    @Override
    public void addGroup(GroupDto groupDto) throws ServiceException {
        var newGroup = GroupMapper.toGroup(groupDto);

        try {
            if (Boolean.FALSE.equals(ifExist(newGroup.getGroupName()))) {
                groupRepository.addGroup(newGroup);
                var lastGroupFromDB = groupRepository.getLastGroup();

                if (newGroup.equals(lastGroupFromDB)) {
                    log.info("Group with id was added");
                } else {
                    log.error("Group with id wasn`t added");
                }
            } else {
                log.info(String.format(GROUP_WITH_ID_EXISTS, newGroup.getGroupId()));

                throw new ServiceException(String.format(GROUP_WITH_ID_EXISTS, newGroup.getGroupId()));
            }

        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);

            throw new ServiceException(e);
        }
    }

    @Override
    public List<GroupDto> getAllGroups() throws ServiceException {

        try {
            var allGroupsList = groupRepository.getAllGroups()
                    .stream()
                    .map(GroupMapper::toDto)
                    .toList();
            log.info("All groups were received");

            return allGroupsList;

        } catch (RepositoryException e) {
            String msg = "There are no groups in the database";
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public GroupDto getGroupById(int groupId) throws ServiceException {

        try {
            var groupDto = GroupMapper.toDto(groupRepository.getGroupById(groupId));
            log.info("Group with id {} was received", groupId);

            return groupDto;

        } catch (RepositoryException e) {
            var msg = String.format(GROUP_WITH_ID_NOT_EXIST, groupId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) throws ServiceException {

        try {
            var groupList = groupRepository.getGroupsSmallerThen(numberOfStudents)
                    .stream()
                    .map(GroupMapper::toDto)
                    .toList();
            log.info("Groups with number of students less then {} were received", numberOfStudents);

            return groupList;

        } catch (RepositoryException e) {
            var msg = String.format("There are no groups with number of students less then %d",
                    numberOfStudents);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public GroupDto getGroupByName(String groupName) throws ServiceException {

        try {
            var grouddto = GroupMapper.toDto(groupRepository.getGroupByName(groupName));
            log.info("Group with name {} was received", groupName);

            return grouddto;

        } catch (RepositoryException e) {
            var msg = String.format("Group with name %s doesn't exist", groupName);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public List<StudentDto> getStudentsByGroup(int groupId) throws ServiceException {

        try {
            var studentsList = groupRepository.getStudentsByGroup(groupId)
                    .stream()
                    .map(StudentMapper::toDto)
                    .toList();
            log.info("Students from group with id {} were received", groupId);

            return studentsList;

        } catch (RepositoryException e) {
            var msg = String.format("There are no students in group with id %d", groupId);
            log.error(msg, e);

            throw new ServiceException(msg, e);
        }
    }

    @Override
    public Boolean ifExist(String groupName) {

        try {
            groupRepository.getGroupByName(groupName);

            return true;

        } catch (RepositoryException e) {
            log.info(e.getMessage(), e);

            return false;
        }
    }
}
