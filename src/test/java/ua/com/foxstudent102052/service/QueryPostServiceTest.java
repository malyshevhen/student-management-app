package ua.com.foxstudent102052.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxstudent102052.dao.interfaces.PostDAO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class QueryPostServiceTest {
    private PostDAO postDAO;
    private QueryPostService queryPostService;

    @BeforeEach
    void setUp() {
        postDAO = mock(PostDAO.class);
        queryPostService = new QueryPostService(postDAO);
    }

    @Test
    void Method_executeQuery_shouldPassToDAO_doPostArgument() {
        // given
        var query = "Some query";

        // when
        queryPostService.executeQuery(query);

        // then
        verify(postDAO).doPost(query);
    }
}
