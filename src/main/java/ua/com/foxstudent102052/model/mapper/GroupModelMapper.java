package ua.com.foxstudent102052.model.mapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.dto.GroupDto;

import java.util.List;

@Slf4j
public class GroupModelMapper {

    private GroupModelMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GroupDto toGroupDto(Group group) {
        try {
            return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
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
                .id(groupDto.getId())
                .name(groupDto.getName())
                .build();
        } catch (NullPointerException e) {
            log.error("GroupMapper.toGroup() - NullPointerException: " + e.getMessage());

            return Group.builder().build();
        }

    }
}
