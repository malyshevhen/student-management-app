package ua.com.foxstudent102052.dao.testinit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

@ExtendWith(MockitoExtension.class)
public class TestDataRepositoryTest {
    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private RecordDao recordDaoImpl;

    private TestDataRepository testDataRepository;

    @BeforeEach
    void setUp() {
        testDataRepository = new TestDataRepository(studentDao, courseDao, groupDao, recordDaoImpl);
    }

    @Test
    void method_postTestRecords_shouldPassToDaosRecords() {
        // given
        var students = List.of(
                new Student(0, 0, "John", "Doe"),
                new Student(0, 0, "Jane", "Doe"));
        var groups = List.of(
                new Group(0, "G1"),
                new Group(0, "G2"));
        var courses = List.of(
                new Course(0, "C1", "Some description 1"),
                new Course(0, "C2", "Some description 2"));

        // when
        when(groupDao.getAll()).thenReturn(groups);

        testDataRepository.postTestRecords(students, courses, groups);

        // then
        verify(studentDao, times(2)).addStudent(any(Student.class));
        verify(courseDao, times(2)).addCourse(any(Course.class));
        verify(groupDao, times(2)).addGroup(any(Group.class));
    }
}
