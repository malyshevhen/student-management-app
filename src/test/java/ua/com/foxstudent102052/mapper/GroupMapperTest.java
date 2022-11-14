package ua.com.foxstudent102052.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.com.foxstudent102052.model.Group;
import ua.com.foxstudent102052.model.GroupDto;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupMapperTest {

    @DisplayName("Method groupToDto() should return GroupDto object")
    @ParameterizedTest
    @MethodSource
    void groupToDto(Group group, GroupDto expected) {
        GroupDto actual = GroupMapper.groupToDto(group);
        
        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> groupToDto() {
        return Stream.of(
                Arguments.of(
                        new Group(1, "Java"),
                        new GroupDto(1, "Java", List.of())
                ),
                Arguments.of(
                        new Group(2, "C++"),
                        new GroupDto(2, "C++", List.of())
                ),
                Arguments.of(
                        new Group(0, null),
                        new GroupDto(0, null, List.of())
                ),
                Arguments.of(
                        new Group(),
                        new GroupDto(0, null, List.of())
                ),
                Arguments.of(
                        null,
                        new GroupDto()
                )
        );
    }

    @DisplayName("Method dtoToGroup() should return Group object")
    @ParameterizedTest
    @MethodSource
    void dtoToGroup(GroupDto groupDto, Group expected) {
        Group actual = GroupMapper.dtoToGroup(groupDto);
        
        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> dtoToGroup() {
        return Stream.of(
                Arguments.of(
                        new GroupDto(1, "Java", List.of()),
                        new Group(1, "Java")
                ),
                Arguments.of(
                        new GroupDto(2, "C++", List.of()),
                        new Group(2, "C++")
                ),
                Arguments.of(
                        new GroupDto(0, null, List.of()),
                        new Group(0, null)
                ),
                Arguments.of(
                        new GroupDto(),
                        new Group()
                ),
                Arguments.of(
                        null,
                        new Group()
                )
        );
    }
}
