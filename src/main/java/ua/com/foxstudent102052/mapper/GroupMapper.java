package ua.com.foxstudent102052.mapper;

import lombok.NonNull;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;

import java.util.List;

public class GroupMapper {

    private GroupMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GroupDto groupToDto(@NonNull Group group) {
        return new GroupDto(
                group.getGroupId(),
                group.getGroupName(),
                List.of());
    }

    public static Group dtoTGroup(@NonNull GroupDto groupDto) {
        return new Group(
                groupDto.getName());
    }
}
