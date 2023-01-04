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
                .id(course.courseId())
                .name(course.courseName())
                .description(course.courseDescription())
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
                .courseId(courseDto.getId())
                .courseName(courseDto.getName())
                .courseDescription(courseDto.getDescription())
                .build();
        } catch (NullPointerException e) {
            log.error("CourseMapper.toCourse() - NullPointerException: " + e.getMessage());

            return Course.builder().build();
        }
    }
}
