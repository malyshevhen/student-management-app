package ua.com.foxstudent102052.mapper;

import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;

import java.util.List;

public class GroupMapper {

    private GroupMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GroupDto groupToDto(Group group) {
        try{
            return new GroupDto(
                group.getGroupId(),
                group.getGroupName(),
                List.of());
            
        } catch (NullPointerException e) {
            return new GroupDto();
        }
    }

    public static Group dtoToGroup(GroupDto groupDto) {
        try{
            return new Group(groupDto.getId(), groupDto.getName());
            
        } catch (NullPointerException e) {
            return new Group();
        }

    }
}
