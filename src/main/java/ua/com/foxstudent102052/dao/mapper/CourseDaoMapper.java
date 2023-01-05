package ua.com.foxstudent102052.dao.mapper;

import ua.com.foxstudent102052.model.entity.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoMapper {
    private CourseDaoMapper() {
    }

    public static Course mapToCourse(ResultSet courseResultSet) throws SQLException {
        return Course.builder()
            .id(courseResultSet.getInt(1))
            .name(courseResultSet.getString(2))
            .description(courseResultSet.getString(3))
            .build();
    }

    public static List<Course> mapToCourses(ResultSet coursesResultSet) throws SQLException {
        var courses = new ArrayList<Course>();

        while (coursesResultSet.next()) {
            courses.add(mapToCourse(coursesResultSet));
        }

        return courses;
    }
}
