package ua.com.foxstudent102052.table;

import ua.com.foxstudent102052.table.interfaces.TableBuilder;

import java.util.List;

public class TableFactory {
    private TableBuilder tableBuilder;

    public <T> String buildTable(List<T> entityList, TableBuilder<T> tableBuilder) {
        setTableBuilder(tableBuilder);
        return tableBuilder.buildTable(entityList);
    }

    private <T> void setTableBuilder(TableBuilder<T> tableBuilder) {
        this.tableBuilder = tableBuilder;
    }
}
