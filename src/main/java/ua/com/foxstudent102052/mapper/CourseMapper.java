package ua.com.foxstudent102052.mapper;

import lombok.NonNull;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.CourseDto;

import java.util.List;

public class CourseMapper {

    private CourseMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CourseDto courseToDto(@NonNull Course course) {
        return new CourseDto(
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseDescription(),
                List.of());
    }

    public static Course dtoToCourse(@NonNull CourseDto courseDto) {
        return new Course(
                courseDto.getName(),
                courseDto.getDescription());
    }
}
