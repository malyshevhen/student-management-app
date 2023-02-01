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
import ua.com.foxstudent102052.utils.RandomModelCreator;

@ExtendWith(MockitoExtension.class)
public class TestDataRepositoryTest {
    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private RandomModelCreator randomModelCreator;

    private TestDataRepository testDataRepository;

    @BeforeEach
    void setUp() {
        testDataRepository = new TestDataRepository(studentDao, courseDao, groupDao, randomModelCreator);
    }

    @Test
    void method_postTestRecords_shouldPassToDaosRecords() {
        // given
        var students = List.of(
                new Student(0, null, "John", "Doe", null),
                new Student(0, null, "Jane", "Doe", null));
        var groups = List.of(
                new Group(0, "G1", null),
                new Group(0, "G2", null));
        var courses = List.of(
                new Course(0, "C1", "Some description 1", null),
                new Course(0, "C2", "Some description 2", null));

        // when
        when(groupDao.getAll()).thenReturn(groups);

        testDataRepository.postTestRecords(students, courses, groups);

        // then
        verify(studentDao, times(2)).addStudent(any(Student.class));
        verify(courseDao, times(2)).addCourse(any(Course.class));
        verify(groupDao, times(2)).addGroup(any(Group.class));
    }
}
