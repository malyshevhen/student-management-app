package ua.com.foxstudent102052.table;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;
import ua.com.foxstudent102052.table.impl.ExpandedGroupTableBuilder;
import ua.com.foxstudent102052.table.impl.ExpandedStudentTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedCourseTableBuilder;
import ua.com.foxstudent102052.table.impl.ReducedGroupTableBuilder;
import ua.com.foxstudent102052.table.interfaces.TableBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TableFactoryTest {
    private static final String REDUCED_GROUP_TABLE = """
        +=====+==========+
        |ID   |NAME      |
        +=====+==========+
        |1    |Group 1   |
        +-----+----------+
        |2    |Group 2   |
        +-----+----------+
        |3    |Bad group |
        +=====+==========+
        """;
    private static final String REDUCED_COURSE_TABLE = """
        +=====+===============+
        |ID   |NAME           |
        +=====+===============+
        |1    |Math           |
        +-----+---------------+
        |2    |English        |
        +-----+---------------+
        |3    |Physics        |
        +-----+---------------+
        |4    |Chemistry      |
        +=====+===============+
        """;

    private static final String EXPANDED_GROUP_TABLE = """
        +=====+==========+==================================================+
        |ID   |NAME      |STUDENTS                                          |
        +=====+==========+==================================================+
        |1    |Group 1   |John Doe                                          |
        |     |          |Jane Doe                                          |
        |     |          |Jane Depp                                         |
        +-----+----------+--------------------------------------------------+
        |2    |Group 2   |John Smith                                        |
        |     |          |Jane Smith                                        |
        +-----+----------+--------------------------------------------------+
        |3    |Bad group |No students                                       |
        +=====+==========+==================================================+
        """;
    private static final String EXPANDED_STUDENT_TABLE = """
        +=====+==========+==========+==========+========================================+
        |ID   |GROUP     |FIRST NAME|LAST NAME |COURSES                                 |
        +=====+==========+==========+==========+========================================+
        |1    |Group 1   |John      |Doe       |Math                                    |
        +-----+----------+----------+----------+----------------------------------------+
        |2    |Group 1   |Jane      |Doe       |Math                                    |
        |     |          |          |          |English                                 |
        +-----+----------+----------+----------+----------------------------------------+
        |3    |Group 2   |John      |Smith     |Math                                    |
        |     |          |          |          |English                                 |
        |     |          |          |          |Physics                                 |
        +-----+----------+----------+----------+----------------------------------------+
        |4    |Group 2   |Jane      |Smith     |Math                                    |
        |     |          |          |          |English                                 |
        |     |          |          |          |Physics                                 |
        |     |          |          |          |Chemistry                               |
        +-----+----------+----------+----------+----------------------------------------+
        |5    |No group  |John      |Depp      |Math                                    |
        +-----+----------+----------+----------+----------------------------------------+
        |6    |Group 1   |Jane      |Depp      |No courses                              |
        +=====+==========+==========+==========+========================================+
        """;

    private static List<StudentDto> studentDtoList;
    private static List<CourseDto> courseDtoList;
    private static List<GroupDto> groupDtoList;

    private final TableFactory tableFactory = new TableFactory();

    @BeforeAll
    static void setUp() {
        courseDtoList = new LinkedList<>();
        courseDtoList.add(new CourseDto(1, "Math", "Some description", null));
        courseDtoList.add(new CourseDto(2, "English", "Some description", null));
        courseDtoList.add(new CourseDto(3, "Physics", "Some description", null));
        courseDtoList.add(new CourseDto(4, "Chemistry", "Some description", null));

        groupDtoList = new LinkedList<>();
        groupDtoList.add(new GroupDto(1, "Group 1", null));
        groupDtoList.add(new GroupDto(2, "Group 2", null));
        groupDtoList.add(new GroupDto(3, "Bad group", null));

        studentDtoList = new LinkedList<>();
        studentDtoList.add(new StudentDto(1, "John", "Doe", groupDtoList.get(0), courseDtoList.subList(0, 1)));
        studentDtoList.add(new StudentDto(2, "Jane", "Doe", groupDtoList.get(0), courseDtoList.subList(0, 2)));
        studentDtoList.add(new StudentDto(3, "John", "Smith", groupDtoList.get(1), courseDtoList.subList(0, 3)));
        studentDtoList.add(new StudentDto(4, "Jane", "Smith", groupDtoList.get(1), courseDtoList.subList(0, 4)));
        studentDtoList.add(new StudentDto(5, "John", "Depp", new GroupDto(), courseDtoList.subList(0, 1)));
        studentDtoList.add(new StudentDto(6, "Jane", "Depp", groupDtoList.get(0), List.of()));

        courseDtoList.forEach(courseDto -> courseDto.setStudentList(
            studentDtoList.stream()
                .filter(studentDto -> studentDto.getCoursesList().contains(courseDto))
                .toList()));

        groupDtoList.forEach(groupDto -> groupDto.setStudentList(
            studentDtoList.stream()
                .filter(studentDto -> studentDto.getGroup() == groupDto)
                .toList()));
    }

    @ParameterizedTest
    @MethodSource
    <T> void buildTable_ShouldReturnDefaultResult_WhenDtoListIsEmpty(List<T> dtoList, TableBuilder<T> tableBuilder) {
        // given
        var expected = "List is empty";

        // when
        var actual = tableFactory.buildTable(dtoList, tableBuilder);

        // then
        assertEquals(expected, actual);
    }

    public static Stream<Arguments> buildTable_ShouldReturnDefaultResult_WhenDtoListIsEmpty() {
        return Stream.of(
            Arguments.of(List.of(), new ReducedGroupTableBuilder()),
            Arguments.of(List.of(), new ReducedCourseTableBuilder()),
            Arguments.of(List.of(), new ExpandedGroupTableBuilder()),
            Arguments.of(List.of(), new ExpandedStudentTableBuilder()));
    }

    @ParameterizedTest
    @MethodSource
    <T> void buildTable_ShouldReturnTableAsExpected(List<T> dtoList, TableBuilder<T> tableBuilder, String expected) {
        // when
        var actual = tableFactory.buildTable(dtoList, tableBuilder);

        // then
        assertEquals(expected, actual);
    }

    public static Stream<Arguments> buildTable_ShouldReturnTableAsExpected() {
        return Stream.of(
            Arguments.of(groupDtoList, new ReducedGroupTableBuilder(), REDUCED_GROUP_TABLE),
            Arguments.of(courseDtoList, new ReducedCourseTableBuilder(), REDUCED_COURSE_TABLE),
            Arguments.of(groupDtoList, new ExpandedGroupTableBuilder(), EXPANDED_GROUP_TABLE),
            Arguments.of(studentDtoList, new ExpandedStudentTableBuilder(), EXPANDED_STUDENT_TABLE));
    }
}
