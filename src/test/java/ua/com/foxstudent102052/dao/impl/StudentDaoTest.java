package ua.com.foxstudent102052.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxstudent102052.dao.impl.config.AbstractTestContainerIT;
import ua.com.foxstudent102052.dao.interfaces.StudentRepository;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

class StudentDaoTest extends AbstractTestContainerIT {

    @Autowired
    private StudentRepository studentDao;

    @Test
    @Transactional
    void MethodAddStudent_ShouldAddStudentToDb() {
        // given
        var newStudent = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        // when
        studentDao.save(newStudent);
        int expected = studentDao.findAll().size();
        int actual = 11;

        // then
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetAllStudents_ShouldReturnAllStudents() {
        // when
        var actual = studentDao.findAll().size();

        // then
        assertEquals(10, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByCourseId_ShouldReturnStudentByGroupId() {
        var expected = List.of(
                Student.builder()
                        .id(1L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Leia")
                        .lastName("Organa")
                        .build(),
                Student.builder()
                        .id(2L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Luke")
                        .lastName("Skywalker")
                        .build(),
                Student.builder()
                        .id(3L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Han")
                        .lastName("Solo")
                        .build(),
                Student.builder()
                        .id(4L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Padme")
                        .lastName("Amidala")
                        .build());

        var actual = studentDao.findByGroupId(1L);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByGroup_ShouldReturnStudentByCourseId() {
        var expected = List.of(
                Student.builder()
                        .id(1L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Leia")
                        .lastName("Organa")
                        .build(),
                Student.builder()
                        .id(2L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Luke")
                        .lastName("Skywalker")
                        .build(),
                Student.builder()
                        .id(4L)
                        .group(new Group(1L, "Group 1", List.of()))
                        .firstName("Padme")
                        .lastName("Amidala")
                        .build(),
                Student.builder()
                        .id(5L)
                        .group(new Group(2L, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Maul")
                        .build(),
                Student.builder()
                        .id(9L)
                        .group(new Group(2L, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Vader")
                        .build(),
                Student.builder()
                        .id(10L)
                        .group(new Group(3L, "Group 3", List.of()))
                        .firstName("Jah Jah")
                        .lastName("Binks")
                        .build());

        var actual = studentDao.findByCourseId(1L);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByNameAndCourse_ShouldReturnListOfStudents_ByStudentNameAndCourseId() {
        var expected = List.of(
                Student.builder()
                        .id(5L)
                        .group(new Group(2L, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Maul")
                        .build(),
                Student.builder()
                        .id(9L)
                        .group(new Group(2L, "Group 2", List.of()))
                        .firstName("Dart")
                        .lastName("Vader")
                        .build());

        var actual = studentDao.findByNameAndCourseId("Dart", 1L);

        assertEquals(expected, actual);
    }
}
