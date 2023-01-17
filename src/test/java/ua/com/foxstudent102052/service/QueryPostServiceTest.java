package ua.com.foxstudent102052.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.dao.interfaces.PostDAO;

@ExtendWith(MockitoExtension.class)
class QueryPostServiceTest {

    @Mock
    PostDAO postDAO;

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
