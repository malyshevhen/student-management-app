package ua.com.foxstudent102052.table.impl;

import ua.com.foxstudent102052.model.dto.GroupDto;

public class ReducedGroupTableBuilder extends DtoTableBuilderImpl<GroupDto>{
    private static final String ROW = "|%-5s|%-10s|";
    private static final String HEATHER = """
        +=====+==========+
        |ID   |NAME      |
        +=====+==========+""";
    private static final String FOOTER = "+=====+==========+";
    private static final String SEMI_FOOTER = "+-----+----------+";

    @Override
    protected String buildInfoBlock(GroupDto group) {
        var groupTableBlockBuilder = new StringBuilder();
        var id = String.valueOf(group.getId());
        var name = group.getName();

        return groupTableBlockBuilder.append(String.format(ROW, id, name))
            .append("\n")
            .append(SEMI_FOOTER)
            .append("\n").toString();
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
