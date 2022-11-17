package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.mapper.GroupMapper;
import ua.com.foxstudent102052.mapper.StudentMapper;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;
import ua.com.foxstudent102052.repository.GroupRepository;
import ua.com.foxstudent102052.repository.exception.DAOException;
import ua.com.foxstudent102052.service.exception.GroupAlreadyExistException;
import ua.com.foxstudent102052.service.exception.NoSuchGroupExistsException;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final String GROUP_WITH_ID_NOT_EXIST = "Group with id %d doesn't exist";
    private static final String GROUP_WITH_ID_EXISTS = "Group with id %d already exists";
    private final GroupRepository groupRepository;

    @Override
    public void addGroup(GroupDto groupDto) {

        try {
            groupRepository.addGroup(GroupMapper.toGroup(groupDto));

        } catch (DAOException e) {
            String msg = String.format(GROUP_WITH_ID_EXISTS, groupDto.getId());
            log.error(msg, e);

            throw new GroupAlreadyExistException(msg);
        }
    }

    @Override
    public List<GroupDto> getAllGroups() {

        try {
            return groupRepository.getAllGroups()
                .stream()
                .map(GroupMapper::toDto)
                .toList();

        } catch (DAOException e) {
            String msg = "There are no groups in the database";
            log.error(msg, e);

            throw new NoSuchGroupExistsException(msg);
        }
    }

    @Override
    public GroupDto getGroupById(int groupId) {

        try {
            return GroupMapper.toDto(groupRepository.getGroupById(groupId));

        } catch (DAOException e) {
            String msg = String.format(GROUP_WITH_ID_NOT_EXIST, groupId);
            log.error(msg, e);

            throw new NoSuchGroupExistsException(msg);
        }
    }

    @Override
    public List<GroupDto> getGroupsSmallerThen(int numberOfStudents) {

        try {
            return groupRepository.getGroupsSmallerThen(numberOfStudents)
                .stream()
                .map(GroupMapper::toDto)
                .toList();

        } catch (DAOException e) {
            String msg = String.format("There are no groups with number of students less then %d",
                numberOfStudents);
            log.error(msg, e);

            throw new NoSuchGroupExistsException(msg);
        }
    }

    @Override
    public GroupDto getGroupByName(String groupName) {

        try {
            return GroupMapper.toDto(groupRepository.getGroupByName(groupName));

        } catch (DAOException e) {
            String msg = String.format("Group with name %s doesn't exist", groupName);
            log.error(msg, e);

            throw new NoSuchGroupExistsException(msg);
        }
    }

    @Override
    public List<StudentDto> getStudentsByGroup(int groupId) {

        try {
            return groupRepository.getStudentsByGroup(groupId)
                .stream()
                .map(StudentMapper::toDto)
                .toList();

        } catch (DAOException e) {
            String msg = String.format("There are no students in group with id %d", groupId);
            log.error(msg, e);

            throw new NoSuchGroupExistsException(msg);
        }
    }
}
