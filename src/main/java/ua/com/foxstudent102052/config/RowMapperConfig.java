package ua.com.foxstudent102052.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

@Configuration
public class RowMapperConfig {

    @Bean(name = "courseRowMapper")
    public RowMapper<Course> courseRowMapper() {
        return BeanPropertyRowMapper.newInstance(Course.class);
    }

    @Bean(name = "groupRowMapper")
    public RowMapper<Group> groupRowMapper() {
        return BeanPropertyRowMapper.newInstance(Group.class);
    }

    @Bean(name = "studentRowMapper")
    public RowMapper<Student> studentRowMapper() {
        return BeanPropertyRowMapper.newInstance(Student.class);
    }
}
