package ua.com.foxstudent102052.facade;

import ua.com.foxstudent102052.model.CourseDto;
import ua.com.foxstudent102052.model.GroupDto;
import ua.com.foxstudent102052.model.StudentDto;

import java.util.List;

public class TableBuilder {
    private static final String STUDENT_LIST_HEADER = """
            +=====+==========+==========+==========+========================================+
            |ID   |GROUP     |FIRST NAME|LAST NAME |COURSES                                 |
            +=====+==========+==========+==========+========================================+""";

    private static final String STUDENT_LIST_ROW = "|%-5s|%-10s|%-10s|%-10s|%-40s|";

    private static final String STUDENT_LIST_FOOTER = "+=====+==========+==========+==========+========================================+";

    private static final String STUDENT_LIST_SEMI_FOOTER = "+-----+----------+----------+----------+----------------------------------------+";

    private static final String GROUP_STUDENTS_LIST_HEADER = """
            +=====+==========+==================================================+
            |ID   |NAME      |STUDENTS                                          |
            +=====+==========+==================================================+""";

    private static final String GROUP_STUDENTS_LIST_ROW = "|%-5s|%-10s|%-50s|";

    private static final String GROUP_STUDENT_LIST_FOOTER = "+=====+==========+==================================================+";

    private static final String GROUP_STUDENTS_LIST_SEMI_FOOTER = "+-----+----------+--------------------------------------------------+";

    private static final String COURSE_LIST_HEADER = """
            +=====+===============+================================================================================+
            |ID   |NAME           |DESCRIPTION                                                                     |
            +=====+===============+================================================================================+""";

    private static final String COURSE_LIST_ROW = "|%-5s|%-15s|%-80s|";

    private static final String COURSE_LIST_FOOTER = "+=====+===============+================================================================================+";

    private static final String COURSE_LIST_SEMI_FOOTER = "+-----+---------------+--------------------------------------------------------------------------------+";

    private static final String COURSE_STUDENTS_LIST_HEADER = """
            +=====+==========+========================================+====================+
            |ID   |NAME      |DESCRIPTION                             |STUDENTS            |
            +=====+==========+========================================+====================+""";

    private static final String COURSE_STUDENTS_LIST_ROW = "|%-5s|%-10s|%-40s|-20%s|";

    private static final String COURSE_STUDENTS_LIST_FOOTER = "+=====+==========+========================================+====================+";

    private static final String COURSE_STUDENTS_LIST_SEMI_FOOTER = "+-----+----------+----------------------------------------+--------------------+";
    private static final String GROUP_LIST_HEADER = """
            +=====+==========+
            |ID   |NAME      |
            +=====+==========+""";
    private static final String GROUP_LIST_FOOTER = "+=====+==========+";
    private static final String GROUP_LIST_ROW = "|%-5s|%-10s|";
    private static final String GROUP_LIST_SEMI_FOOTER = "+-----+----------+";

    private TableBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static String createStudentTable(List<StudentDto> students) {

        if (students.isEmpty()) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(STUDENT_LIST_HEADER)
                    .append(System.lineSeparator());

            for (StudentDto student : students) {
                table.append(createStudentTableBlock(student));
            }
            table.append(STUDENT_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }

    }

    public static String createStudentTable(StudentDto student) {

        if (student.getId() == 0) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(STUDENT_LIST_HEADER)
                    .append(System.lineSeparator())
                    .append(createStudentTableBlock(student))
                    .append(STUDENT_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    private static String createStudentTableBlock(StudentDto student) {
        StringBuilder studentBlockBuilder = new StringBuilder();
        String id = String.valueOf(student.getId());
        String group = student.getGroup();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        var courses = student.getCoursesList();

        if (courses.isEmpty()) {
            studentBlockBuilder.append(String.format(STUDENT_LIST_ROW, id, group, firstName, lastName, "No courses"))
                    .append(System.lineSeparator());
        } else {
            for (int i = 0; i < courses.size(); i++) {

                if (i == 0) {
                    studentBlockBuilder.append(String.format(STUDENT_LIST_ROW, id, group, firstName, lastName,
                            courses.get(i).getName()))
                            .append(System.lineSeparator());

                } else {
                    studentBlockBuilder
                            .append(String.format(STUDENT_LIST_ROW, " ", " ", " ", " ", courses.get(i).getName()))
                            .append(System.lineSeparator());
                }
            }
            studentBlockBuilder.append(STUDENT_LIST_SEMI_FOOTER)
                    .append(System.lineSeparator());
        }

        return studentBlockBuilder.toString();
    }

    public static String createGroupTable(GroupDto groupDto) {
        if (groupDto.getId() != 0) {
            StringBuilder groupTableBuilder = new StringBuilder();
            groupTableBuilder.append(GROUP_LIST_HEADER)
                    .append(System.lineSeparator())
                    .append(createGroupTableBlock(groupDto))
                    .append(GROUP_LIST_FOOTER)
                    .append(System.lineSeparator());

            return groupTableBuilder.toString();
        }

        return "";
    }

    public static String createGroupTable(List<GroupDto> groupsDto) {
        if (!groupsDto.isEmpty()) {
            StringBuilder groupTableBuilder = new StringBuilder();
            groupTableBuilder.append(GROUP_LIST_HEADER)
                    .append(System.lineSeparator());

            for (GroupDto groupDto : groupsDto) {
                groupTableBuilder.append(createGroupTableBlock(groupDto));
            }
            groupTableBuilder.append(GROUP_LIST_FOOTER)
                    .append(System.lineSeparator());

            return groupTableBuilder.toString();
        }

        return "";
    }

    private static String createGroupTableBlock(GroupDto groupDto) {
        StringBuilder groupBlockBuilder = new StringBuilder();
        String id = String.valueOf(groupDto.getId());
        String name = groupDto.getName();
        groupBlockBuilder.append(String.format(GROUP_LIST_ROW, id, name))
                .append(System.lineSeparator())
                .append(GROUP_LIST_SEMI_FOOTER)
                .append(System.lineSeparator());

        return groupBlockBuilder.toString();
    }

    public static String createGroupStudentsTable(List<GroupDto> groups) {

        if (groups.isEmpty()) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(GROUP_STUDENTS_LIST_HEADER)
                    .append(System.lineSeparator());

            for (GroupDto group : groups) {
                table.append(buildGroupStudentsTableBlock(group));
            }
            table.append(GROUP_STUDENT_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    public static String createGroupStudentsTable(GroupDto group) {

        if (group.getId() == 0) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(GROUP_STUDENTS_LIST_HEADER)
                    .append(System.lineSeparator())
                    .append(buildGroupStudentsTableBlock(group))
                    .append(GROUP_STUDENT_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    private static String buildGroupStudentsTableBlock(GroupDto group) {
        StringBuilder groupTableBlockBuilder = new StringBuilder();
        String id = String.valueOf(group.getId());
        String name = group.getName();
        var students = group.getStudentList();

        if (students.isEmpty()) {
            groupTableBlockBuilder.append(String.format(GROUP_STUDENTS_LIST_ROW, id, name, "No students"))
                    .append(System.lineSeparator());
        } else {
            for (int i = 0; i < students.size(); i++) {

                if (i == 0) {
                    groupTableBlockBuilder.append(String.format(GROUP_STUDENTS_LIST_ROW, id, name,
                            students.get(i).getFirstName() + " " + students.get(i).getLastName()))
                            .append(System.lineSeparator());

                } else {
                    groupTableBlockBuilder.append(String.format(GROUP_STUDENTS_LIST_ROW, " ", " ",
                            students.get(i).getFirstName() + " " + students.get(i).getLastName()))
                            .append(System.lineSeparator());
                }
            }
            groupTableBlockBuilder.append(GROUP_STUDENTS_LIST_SEMI_FOOTER)
                    .append(System.lineSeparator());
        }
        return groupTableBlockBuilder.toString();
    }

    public static String createCourseTable(List<CourseDto> courses) {

        if (courses.isEmpty()) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(COURSE_LIST_HEADER)
                    .append(System.lineSeparator());

            for (CourseDto course : courses) {
                table.append(buildCourseTableBlock(course));
            }
            table.append(COURSE_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    public static String createCourseTable(CourseDto course) {

        if (course.getId() == 0) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(COURSE_LIST_HEADER)
                    .append(System.lineSeparator())
                    .append(buildCourseTableBlock(course))
                    .append(COURSE_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    private static String buildCourseTableBlock(CourseDto course) {
        StringBuilder table = new StringBuilder();
        String id = String.valueOf(course.getId());
        String name = course.getName();
        String descriptions = course.getDescription();
        var descriptionLines = descriptions.lines().toArray();

        for (int i = 0; i < descriptionLines.length; i++) {
            if (i == 0) {
                table.append(String.format(COURSE_LIST_ROW, id, name, descriptionLines[i]))
                        .append(System.lineSeparator());
            } else {
                table.append(String.format(COURSE_LIST_ROW, " ", " ", descriptionLines[i]))
                        .append(System.lineSeparator());
            }
        }
        table.append(COURSE_LIST_SEMI_FOOTER)
                .append(System.lineSeparator());

        return table.toString();
    }

    public static String createCourseStudentsTable(CourseDto course) {

        if (course.getId() == 0) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(COURSE_STUDENTS_LIST_HEADER)
                    .append(System.lineSeparator())
                    .append(buildCourseStudentTableBlock(course))
                    .append(COURSE_STUDENTS_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    public static String createCourseStudentsTable(List<CourseDto> courses) {

        if (courses.isEmpty()) {
            return "";

        } else {
            StringBuilder table = new StringBuilder();
            table.append(COURSE_STUDENTS_LIST_HEADER).append(System.lineSeparator());

            for (CourseDto course : courses) {
                table.append(buildCourseStudentTableBlock(course));
            }
            table.append(COURSE_STUDENTS_LIST_FOOTER)
                    .append(System.lineSeparator());

            return table.toString();
        }
    }

    private static String buildCourseStudentTableBlock(CourseDto course) {
        StringBuilder table = new StringBuilder();
        String id = String.valueOf(course.getId());
        String name = course.getName();
        String description = course.getDescription();
        var students = course.getStudentsList();

        if (students.isEmpty()) {
            table.append(String.format(COURSE_STUDENTS_LIST_ROW, id, name, description, "No students"))
                    .append(System.lineSeparator());
        } else {

            for (int i = 0; i < students.size(); i++) {

                if (i == 0) {
                    table.append(String.format(COURSE_STUDENTS_LIST_ROW, id, name, description,
                            students.get(i).getFirstName() + " " + students.get(i).getLastName()))
                            .append(System.lineSeparator());
                } else {
                    table.append(String.format(COURSE_STUDENTS_LIST_ROW, " ", " ", " ",
                            students.get(i).getFirstName() + " " + students.get(i).getLastName()))
                            .append(System.lineSeparator());
                }
                table.append(COURSE_STUDENTS_LIST_SEMI_FOOTER)
                        .append(System.lineSeparator());
            }
        }

        return table.toString();
    }
}
