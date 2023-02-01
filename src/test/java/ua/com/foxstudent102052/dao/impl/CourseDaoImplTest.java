package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.persistence.EntityManager;
import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.model.entity.Course;

class CourseDaoImplTest extends AbstractTestContainerIT {

    private final CourseDao courseDao;

    @Autowired
    public CourseDaoImplTest(EntityManager entityManager) {
        courseDao = new CourseDaoImpl();
        ReflectionTestUtils.setField(courseDao, "entityManager", entityManager);
    }

    @Test
    void MethodAddCourse_ShouldAddCourseToDb() {
        // given
        var course = Course.builder()
                .courseName("Course 4")
                .courseDescription("Some courseDescription for course 4")
                .build();

        // when
        courseDao.addCourse(course);

        // then
        var allCourses = courseDao.getAll();

        assertEquals(4, allCourses.size());
    }

    @Test
    void MethodGetCourses_ShouldReturnListOfAllCoursesFromDB() {
        // when
        var allCourses = courseDao.getAll();

        // then
        assertEquals(3, allCourses.size());
    }

    @Test
    void MethodGetCourse_ById_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
                .courseId(1)
                .courseName("Course 1")
                .courseDescription("Some description for course 1")
                .build();

        // when
        var actual = courseDao.getCourseById(1).get();

        // then
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void MethodGetCourse_ByName_ShouldReturnCourseFromDb() {
        // given
        var expected = Course.builder()
                .courseId(1)
                .courseName("Course 1")
                .courseDescription("Some description for course 1")
                .build();

        // when
        var actual = courseDao.getCourseByName(expected.getCourseName()).orElseThrow();

        // then
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void MethodGetCourses_ByStudentId_ShouldReturnCourseListFromDb() {
        // given
        var expected = List.of(
                Course.builder()
                        .courseId(1)
                        .courseName("Course 1")
                        .courseDescription("Some description for course 1")
                        .build(),
                Course.builder()
                        .courseId(2)
                        .courseName("Course 2")
                        .courseDescription("Some description for course 2")
                        .build());

        // when
        var actual = courseDao.getCoursesByStudentId(2);

        // then
        assertEquals(expected.toString(), actual.toString());
    }
}
