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

        var actual = studentDao.findByGroupId(1);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void MethodGetStudentsByGroup_ShouldReturnStudentByCourseId() {
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

        var actual = studentDao.findByCourseId(1);

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

        var actual = studentDao.findByNameAndCourseId("Dart", 1);

        assertEquals(expected, actual);
    }
}
