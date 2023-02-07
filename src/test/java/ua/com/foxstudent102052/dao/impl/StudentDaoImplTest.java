package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

class StudentDaoImplTest extends AbstractTestContainerIT {

    private final StudentDao studentDao;

    @Autowired
    public StudentDaoImplTest(EntityManager entityManager) {
        studentDao = new StudentDaoImpl();
        ReflectionTestUtils.setField(studentDao, "entityManager", entityManager);
    }

    @Test
    @Transactional
    void MethodAddStudent_ShouldAddStudentToDb() {
        // given
        var newStudent = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        // when
        studentDao.addStudent(newStudent);
        int expected = studentDao.getAll().size();
        int actual = 11;

        // then
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodAddStudentToCourse_ShouldAddStudentToNewCourse() {
        // given
        var expected = Student.builder()
                .studentId(1)
                .group(new Group(1, "Group 1", List.of()))
                .firstName("Leia")
                .lastName("Organa")
                .build();

        // when
        studentDao.addStudentToCourse(1, 2);
        var actual = studentDao.getStudentsByCourse(1).get(0);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetAllStudents_ShouldReturnAllStudents() {
        // when
        var actual = studentDao.getAll().size();

        // then
        assertEquals(10, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByCourseId_ShouldReturnStudentByCourseId() {
        var expected = List.of(
                Student.builder()
                        .studentId(1)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Leia")
                        .lastName("Organa")
                        .build(),
                Student.builder()
                        .studentId(2)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Luke")
                        .lastName("Skywalker")
                        .build(),
                Student.builder()
                        .studentId(3)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Han")
                        .lastName("Solo")
                        .build(),
                Student.builder()
                        .studentId(4)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Padme")
                        .lastName("Amidala")
                        .build());

        var actual = studentDao.getStudentsByGroup(1);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByGroup_ShouldReturnStudentByGroupId() {
        var expected = List.of(
                Student.builder()
                        .studentId(1)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Leia")
                        .lastName("Organa")
                        .build(),
                Student.builder()
                        .studentId(2)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Luke")
                        .lastName("Skywalker")
                        .build(),
                Student.builder()
                        .studentId(4)
                        .group(new Group(1, "Group 1", List.of()))
                        .firstName("Padme")
                        .lastName("Amidala")
                        .build(),
                Student.builder()
                        .studentId(5)
                        .group(new Group(2, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Maul")
                        .build(),
                Student.builder()
                        .studentId(9)
                        .group(new Group(2, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Vader")
                        .build(),
                Student.builder()
                        .studentId(10)
                        .group(new Group(3, "Group 3", List.of()))
                        .firstName("Jah Jah")
                        .lastName("Binks")
                        .build());

        var actual = studentDao.getStudentsByCourse(1);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents_ByStudentNameAndCourseId() {
        var expected = List.of(
                Student.builder()
                        .studentId(5)
                        .group(new Group(2, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Maul")
                        .build(),
                Student.builder()
                        .studentId(9)
                        .group(new Group(2, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Vader")
                        .build());

        var actual = studentDao.getStudentsByNameAndCourse("Dart", 1);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodRemoveStudent_ShouldRemoveStudent_IfItInDataBase() {
        // when
        studentDao.removeStudent(1);

        int expected = 9;
        int actual = studentDao.getAll().size();

        // then
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentById_ShouldReturnStudentFromDb() {
        // given
        var expected = Student.builder()
                .studentId(1)
                .group(new Group(1, "Group 1", List.of()))
                .firstName("Leia")
                .lastName("Organa")
                .build();

        // when
        var actual = studentDao.getStudent(1).get();

        // then
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodRemoveStudentFromCourse_ShouldRemoveStudentCourseRelation_IfExist() {
        // given
        studentDao.removeStudentFromCourse(1, 1);
        var expected = List.of();

        // when
        var actual = studentDao.getStudentsByNameAndCourse("Leia", 1);

        // then
        assertEquals(expected, actual);
    }
}
