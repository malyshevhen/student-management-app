package ua.com.foxstudent102052.table.impl;

import ua.com.foxstudent102052.model.dto.CourseDto;

public class ReducedCourseTableBuilder extends DtoTableBuilderImpl<CourseDto> {
    private static final String ROW = "|%-5s|%-15s|";
    private static final String HEATHER = """
        +=====+===============+
        |ID   |NAME           |
        +=====+===============+""";
    private static final String FOOTER = "+=====+===============+";
    private static final String SEMI_FOOTER = "+-----+---------------+";

    @Override
    protected String buildInfoBlock(CourseDto course) {
        var courseBlockBuilder = new StringBuilder();
        var id = String.valueOf(course.getId());
        var name = course.getName();

        return courseBlockBuilder.append(String.format(ROW, id, name))
            .append(System.lineSeparator())
            .append(SEMI_FOOTER)
            .append(System.lineSeparator())
            .toString();
    }

    @Override
    protected String getHeather() {
        return HEATHER;
    }

    @Override
    protected String getFooter() {
        return FOOTER;
    }
}
