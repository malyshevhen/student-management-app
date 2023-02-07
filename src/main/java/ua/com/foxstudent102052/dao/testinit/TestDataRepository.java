package ua.com.foxstudent102052.dao.testinit;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.dao.interfaces.CourseDao;
import ua.com.foxstudent102052.dao.interfaces.GroupDao;
import ua.com.foxstudent102052.dao.interfaces.StudentDao;
import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;
import ua.com.foxstudent102052.utils.RandomModelCreator;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TestDataRepository {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final RandomModelCreator randomModelCreator;

    public void postTestRecords(List<Student> students, List<Course> courses, List<Group> groups) {
        addCourses(courses);

        addGroups(groups);

        addStudents(students);

        addStudentsToGroups();

        addStudentsToCourses();
    }

    private void addStudentsToCourses() {
        var coursesIds = courseDao.findAll().stream().mapToInt(Course::getCourseId).toArray();
        var studentsIds = studentDao.findAll().stream().mapToInt(Student::getStudentId).toArray();

        Map<Integer, Set<Integer>> studentsCoursesRelations = randomModelCreator
                .getStudentsCoursesRelations(studentsIds, coursesIds, 3);

        for (var entry : studentsCoursesRelations.entrySet()) {
            for (var courseId : entry.getValue()) {
                var student = studentDao.findById(entry.getKey()).orElseThrow();
                var course = courseDao.findById(courseId).orElseThrow();

                student.addCourse(course);

                studentDao.save(student);
                courseDao.save(course);
            }
        }
    }

    private void addStudentsToGroups() {
        Random random = new Random();

        var groups = groupDao.findAll();
        var students = studentDao.findAll();

        for (var student : students) {
            student.setGroup(groups.get(random.nextInt(groups.size())));
        }

        studentDao.saveAll(students);
        groupDao.saveAll(groups);
    }

    private void addStudents(List<Student> students) {
        for (var student : students) {
            try {
                studentDao.save(student);
            } catch (DataAccessException e) {
                log.error("Error while adding test student : " + e.getMessage());
            }
        }
    }

    private void addCourses(List<Course> courses) {
        try {
            for (var course : courses) {
                courseDao.save(course);
            }
        } catch (DataAccessException e) {
            log.error("Error while adding test course : " + e.getMessage());
        }
    }

    private void addGroups(List<Group> groups) {
        try {
            for (var group : groups) {
                groupDao.save(group);
            }
        } catch (DataAccessException e) {
            log.error("Error while adding test group : " + e.getMessage());
        }
    }
}
