package ua.com.foxstudent102052.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostDAOImpl implements ua.com.foxstudent102052.dao.interfaces.PostDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void doPost(String query) {
        jdbcTemplate.execute(query);
    }
}
