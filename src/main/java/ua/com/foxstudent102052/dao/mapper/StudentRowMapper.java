package ua.com.foxstudent102052.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ua.com.foxstudent102052.model.entity.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    @Override
    @Nullable
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Student.builder()
            .id(rs.getInt(1))
            .groupId(rs.getInt(2))
            .firstName(rs.getString(3))
            .lastName(rs.getString(4))
            .build();
    }
}
