package ua.com.foxstudent102052.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseMapperTest {

    @DisplayName("Method courseToDto should return CourseDto object")
    @ParameterizedTest
    @MethodSource
    void courseToDto(Course course, CourseDto expected) {
        CourseDto actual = CourseMapper.courseToDto(course);

        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> courseToDto() {
        return Stream.of(
                Arguments.of(
                        new Course(1, "Java", "Java course"),
                        new CourseDto(1, "Java", "Java course", List.of())
                ),
                Arguments.of(
                        new Course(2, "C++", "C++ course"),
                        new CourseDto(2, "C++", "C++ course", List.of())
                ),
                Arguments.of(
                        new Course(0, null, null),
                        new CourseDto(0, null, null, List.of())
                ),
                Arguments.of(
                        new Course(),
                        new CourseDto(0, null, null, List.of())
                ),
                Arguments.of(
                        null,
                        new CourseDto()
                )
        );
    }
    
    

    @DisplayName("Method dtoToCourse should return Course object")
    @ParameterizedTest
    @MethodSource
    void dtoToCourse(CourseDto courseDto, Course expected) {
        Course actual = CourseMapper.dtoToCourse(courseDto);

        assertEquals(expected, actual);
    }
    
    private static Stream<Arguments> dtoToCourse() {
        return Stream.of(
                Arguments.of(
                        new CourseDto(1, "Java", "Java course", List.of()),
                        new Course(1, "Java", "Java course")
                ),
                Arguments.of(
                        new CourseDto(2, "C++", "C++ course", List.of()),
                        new Course(2, "C++", "C++ course")
                ),
                Arguments.of(
                        new CourseDto(0, null, null, List.of()),
                        new Course(0, null, null)
                ),
                Arguments.of(
                        new CourseDto(),
                        new Course()
                ),
                Arguments.of(
                        null,
                        new Course()
                )
        );
    }
}
