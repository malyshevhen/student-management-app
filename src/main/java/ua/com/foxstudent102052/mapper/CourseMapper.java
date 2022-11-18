package ua.com.foxstudent102052.mapper;

import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;

import java.util.List;

public class CourseMapper {

    private CourseMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CourseDto toDto(Course course) {
        try {
            return new CourseDto(
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCourseDescription(),
                    List.of());

        } catch (NullPointerException e) {
            return new CourseDto();
        }
    }

    public static Course toCourse(CourseDto courseDto) {
        try {
            return new Course(
                    courseDto.getId(),
                    courseDto.getName(),
                    courseDto.getDescription());

        } catch (NullPointerException e) {
            return new Course();
        }
    }
}
