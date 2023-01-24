package ua.com.foxstudent102052.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ua.com.foxstudent102052.dao.interfaces.RecordDao;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecordDaoImpl implements RecordDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void removeAll() throws DataAccessException {
        var query = """
                DELETE FROM students_courses;
                DELETE FROM students;
                DELETE FROM courses;
                DELETE FROM groups;""";

        jdbcTemplate.execute(query);
    }
}
