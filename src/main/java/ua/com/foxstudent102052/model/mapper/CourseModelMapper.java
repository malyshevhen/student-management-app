package ua.com.foxstudent102052.model.mapper;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.dto.CourseDto;

import java.util.List;

@Slf4j
public class CourseModelMapper {

    private CourseModelMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CourseDto toCourseDto(Course course) {
        try {
            return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .studentsList(List.of())
                .build();
        } catch (NullPointerException e) {
            log.error("CourseMapper.toCourseDto() - NullPointerException: " + e.getMessage());

            return new CourseDto();
        }
    }

    public static Course toCourse(CourseDto courseDto) {
        try {
            return Course.builder()
                .id(courseDto.getId())
                .name(courseDto.getName())
                .description(courseDto.getDescription())
                .build();
        } catch (NullPointerException e) {
            log.error("CourseMapper.toCourse() - NullPointerException: " + e.getMessage());

            return Course.builder().build();
        }
    }
}
