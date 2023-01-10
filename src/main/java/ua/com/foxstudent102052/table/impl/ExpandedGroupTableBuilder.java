package ua.com.foxstudent102052.table.impl;

import ua.com.foxstudent102052.model.dto.GroupDto;

public class ExpandedGroupTableBuilder extends DtoTableBuilderImpl<GroupDto> {
    private static final String ROW = "|%-5s|%-10s|%-50s|";
    private static final String HEADER = """
        +=====+==========+==================================================+
        |ID   |NAME      |STUDENTS                                          |
        +=====+==========+==================================================+""";
    private static final String FOOTER = "+=====+==========+==================================================+";
    private static final String SEMI_FOOTER = "+-----+----------+--------------------------------------------------+";

    @Override
    protected String buildInfoBlock(GroupDto group) {
        var infoBlockBuilder = new StringBuilder();
        var id = group.getId();
        var name = group.getName();
        var students = group.getStudentList();

        if (students.isEmpty()) {
            return infoBlockBuilder.append(String.format(ROW, id, name, "No students"))
                .append("\n")
                .append(SEMI_FOOTER)
                .append("\n").toString();
        } else {
            for (int i = 0; i < students.size(); i++) {
                if (i == 0) {
                    infoBlockBuilder.append(String.format(ROW, id, name,
                            students.get(i).getFirstName() + " " + students.get(i).getLastName()))
                        .append("\n");
                } else {
                    infoBlockBuilder.append(String.format(ROW, " ", " ",
                            students.get(i).getFirstName() + " " + students.get(i).getLastName()))
                        .append("\n");
                }
            }
            return infoBlockBuilder.append(SEMI_FOOTER)
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
