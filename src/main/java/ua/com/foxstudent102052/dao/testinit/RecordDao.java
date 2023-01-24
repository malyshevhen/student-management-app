package ua.com.foxstudent102052.dao.testinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RecordDao {

    private final JdbcTemplate jdbcTemplate;

    protected void removeAll() throws DataAccessException {
        var query = """
                DELETE FROM students_courses;
                DELETE FROM students;
                DELETE FROM courses;
                DELETE FROM groups;""";

        jdbcTemplate.execute(query);
    }
}
