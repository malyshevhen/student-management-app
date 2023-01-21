package ua.com.foxstudent102052.table.impl;

import ua.com.foxstudent102052.model.dto.GroupDto;
import ua.com.foxstudent102052.model.dto.StudentDto;

public class ExpandedStudentTableBuilder extends DtoTableBuilderImpl<StudentDto> {
    private static final String ROW = "|%-5s|%-10s|%-10s|%-10s|%-40s|";
    private static final String HEADER = """
            +=====+==========+==========+==========+========================================+
            |ID   |GROUP     |FIRST NAME|LAST NAME |COURSES                                 |
            +=====+==========+==========+==========+========================================+""";
    private static final String FOOTER = "+=====+==========+==========+==========+========================================+";
    private static final String SEMI_FOOTER = "+-----+----------+----------+----------+----------------------------------------+";

    @Override
    protected String buildInfoBlock(StudentDto student) {
        var studentBlockBuilder = new StringBuilder();
        var id = String.valueOf(student.getStudentId());
        var firstName = student.getFirstName();
        var lastName = student.getLastName();
        var group = student.getGroup().equals(new GroupDto()) ? "No group" : student.getGroup().getGroupName();
        var courses = student.getCoursesList();

        if (courses.isEmpty()) {
            return studentBlockBuilder.append(String.format(ROW, id, group, firstName, lastName, "No courses"))
                    .append("\n")
                    .append(SEMI_FOOTER)
                    .append("\n").toString();
        } else {
            for (int i = 0; i < courses.size(); i++) {
                if (i == 0) {
                    studentBlockBuilder.append(String.format(ROW, id, group, firstName, lastName,
                            courses.get(i).getCourseName()))
                            .append("\n");
                } else {
                    studentBlockBuilder
                            .append(String.format(ROW, " ", " ", " ", " ", courses.get(i).getCourseName()))
                            .append("\n");
                }
            }

            return studentBlockBuilder.append(SEMI_FOOTER)
                    .append("\n").toString();
        }
    }

    @Override
    protected String getHeather() {
        return HEADER;
    }

    @Override
    protected String getFooter() {
        return FOOTER;
    }
}
