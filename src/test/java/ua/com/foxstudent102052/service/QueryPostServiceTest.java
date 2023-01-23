package ua.com.foxstudent102052.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxstudent102052.dao.interfaces.PostDao;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QueryPostServiceTest {

    @Mock
    PostDao postDAO;

    @Test
    void Method_executeQuery_shouldPassToDAO_doPostArgument() {
        // given
        QueryPostService queryPostService = new QueryPostService(postDAO);
        var query = "Some query";

        // when
        queryPostService.executeQuery(query);

        // then
        verify(postDAO).doPost(query);
    }
}
