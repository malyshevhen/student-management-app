package ua.com.foxstudent102052.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ua.com.foxstudent102052.model.dto.CourseDto;
import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;

public class RandomModelCreator {
    private static final Random random = new Random();

    private RandomModelCreator() {
        throw new IllegalStateException("Utility class");
    }

    public static List<GroupDto> getGroups(List<String> groupNames) {
        var groupList = new ArrayList<GroupDto>();

        for (String groupName : groupNames) {
            var group = GroupDto.builder()
                    .name(groupName)
                    .build();
            groupList.add(group);
        }

        return groupList;
    }

    public static List<CourseDto> getCourses(List<String[]> courses) {
        var courseDtoArrayList = new ArrayList<CourseDto>();

        for (var courseString : courses) {
            var courseDto = CourseDto.builder()
                    .name(courseString[0])
                    .description(courseString[1])
                    .build();
            courseDtoArrayList.add(courseDto);
        }

        return courseDtoArrayList;
    }

    public static List<StudentDto> getStudents(List<String> names, List<String> surnames, int groupsCount,
            int studentsCount) {
        var studentDtoList = new ArrayList<StudentDto>();

        for (int i = 0; i < studentsCount; i++) {
            studentDtoList.add(StudentDto.builder()
                    .group(new GroupDto(random.nextInt(groupsCount) + 1, " ", List.of()))
                    .firstName(names.get(random.nextInt(names.size())))
                    .lastName(surnames.get(random.nextInt(surnames.size())))
                    .build());
        }

        return studentDtoList;
    }

    public static Map<Integer, Set<Integer>> getStudentsCoursesRelations(int studentsCount, int coursesCount) {
        var studentCourseMap = new HashMap<Integer, Set<Integer>>();

        for (int i = 1; i <= studentsCount; i++) {
            var courses = new HashSet<Integer>();

            for (int j = 1; j <= coursesCount; j++) {
                int courseId = random.nextInt(coursesCount) + 1;

                if (Boolean.FALSE.equals(courses.contains(courseId)) && courses.size() < 3) {
                    courses.add(courseId);
                }
            }
            studentCourseMap.put(i, courses);
        }

        return studentCourseMap;
    }
}
