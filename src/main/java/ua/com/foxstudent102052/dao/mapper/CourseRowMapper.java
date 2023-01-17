package ua.com.foxstudent102052.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import ua.com.foxstudent102052.model.entity.Course;

public class CourseRowMapper implements RowMapper<Course> {

    @Override
    @Nullable
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Course(
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getString("course_description"));
    }
}
