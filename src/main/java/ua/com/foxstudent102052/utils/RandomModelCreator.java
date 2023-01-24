package ua.com.foxstudent102052.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

import ua.com.foxstudent102052.model.entity.Course;
import ua.com.foxstudent102052.model.entity.Group;
import ua.com.foxstudent102052.model.entity.Student;

@Component
public class RandomModelCreator {
    private static final Random random = new Random();

    public List<Group> getGroups(List<String> groupNames) {
        var groupList = new ArrayList<Group>();

        for (String groupName : groupNames) {
            var group = Group.builder()
                    .groupName(groupName)
                    .build();
            groupList.add(group);
        }

        return groupList;
    }

    public List<Course> getCourses(List<String[]> courses) {
        var courseDtoArrayList = new ArrayList<Course>();

        for (var courseString : courses) {
            var course = Course.builder()
                    .courseName(courseString[0])
                    .courseDescription(courseString[1])
                    .build();
            courseDtoArrayList.add(course);
        }

        return courseDtoArrayList;
    }

    public List<Student> getStudents(List<String> names, List<String> surnames, int studentsCount) {
        var studentList = new ArrayList<Student>();

        for (int i = 0; i < studentsCount; i++) {
            studentList.add(Student.builder()
                    .firstName(names.get(random.nextInt(names.size())))
                    .lastName(surnames.get(random.nextInt(surnames.size())))
                    .build());
        }

        return studentList;
    }

    public Map<Integer, Set<Integer>> getStudentsCoursesRelations(int[] studentIds, int[] coursesIds,
            int maxCoursesCount) {

        var studentCourseMap = new HashMap<Integer, Set<Integer>>();

        for (int studentId : studentIds) {
            var courses = new HashSet<Integer>();

            for (int j = 1; j <= maxCoursesCount; j++) {
                int courseId = coursesIds[random.nextInt(maxCoursesCount)];

                if (Boolean.FALSE.equals(courses.contains(courseId)) && courses.size() < 3) {
                    courses.add(courseId);
                }
            }
            studentCourseMap.put(studentId, courses);
        }

        return studentCourseMap;
    }
}
