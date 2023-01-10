package ua.com.foxstudent102052.table.impl;

import ua.com.foxstudent102052.table.interfaces.TableBuilder;

import java.util.List;

abstract class DtoTableBuilderImpl<T> implements TableBuilder<T> {
    private static final String DEFAULT_RESULT = "List is empty";

    @Override
    public String buildTable(List<T> dtoList) {

        if (dtoList.isEmpty()) {
            return DEFAULT_RESULT;
        } else {
            var tableBuilder = new StringBuilder();
            tableBuilder.append(getHeather())
                .append("\n");

            for (T dto : dtoList) {
                tableBuilder.append(buildInfoBlock(dto));
            }

            return tableBuilder
                .replace(tableBuilder.length() - getFooter().length() - 1,
                    tableBuilder.length() - 1,
                    getFooter())
                .toString();
        }
    }

    protected abstract String buildInfoBlock(T dto);

    protected abstract String getHeather();

    protected abstract String getFooter();
}
