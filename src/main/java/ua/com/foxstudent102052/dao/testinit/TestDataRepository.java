package ua.com.foxstudent102052.dao.testinit;

import java.util.List;
import java.util.Random;

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

@Slf4j
@Repository
@RequiredArgsConstructor
public class TestDataRepository {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final RecordDaoImpl recordDaoImpl;

    public void postTestRecords(List<Student> students, List<Course> courses, List<Group> groups) {
        recordDaoImpl.removeAll();

        addCourses(courses);

        addGroups(groups);

        addStudents(students);
    }

    private void addStudents(List<Student> students) {
        Random random = new Random();

        var groupIdArray = groupDao.getAll().stream().mapToInt(Group::getGroupId).toArray();

        students.forEach(student -> student.setGroupId(groupIdArray[random.nextInt(groupIdArray.length)]));

        for (var student : students) {
            try {
                studentDao.addStudent(student);
            } catch (DataAccessException e) {
                log.info("Error while adding student : " + e.getMessage());
            }
        }
    }

    private void addCourses(List<Course> courses) {
        try {
            for (var course : courses) {
                courseDao.addCourse(course);
            }
        } catch (DataAccessException e) {
            log.info("Error while adding course : " + e.getMessage());
        }
    }

    private void addGroups(List<Group> groups) {
        try {
            for (var group : groups) {
                groupDao.addGroup(group);
            }
        } catch (DataAccessException e) {
            log.info("Error while adding group : " + e.getMessage());
        }
    }
}
