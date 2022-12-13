package ua.com.foxstudent102052.mapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.dto.GroupDto;

import java.util.List;

@Slf4j
public class GroupMapper {

    private GroupMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GroupDto toGroupDto(Group group) {
        try {
            return GroupDto.builder()
                .id(group.groupId())
                .name(group.groupName())
                .studentList(List.of())
                .build();
        } catch (NullPointerException e) {
            log.error("GroupMapper.toGroupDto() - NullPointerException: " + e.getMessage());

            return new GroupDto();
        }
    }

    public static Group toGroup(GroupDto groupDto) {
        try {
            return Group.builder()
                .groupId(groupDto.getId())
                .groupName(groupDto.getName())
                .build();
        } catch (NullPointerException e) {
            log.error("GroupMapper.toGroup() - NullPointerException: " + e.getMessage());

            return Group.builder().build();
        }

    }
}
