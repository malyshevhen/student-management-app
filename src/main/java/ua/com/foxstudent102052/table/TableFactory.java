package ua.com.foxstudent102052.table;

import java.util.List;

import ua.com.foxstudent102052.table.interfaces.TableBuilder;

public class TableFactory {
    public <T> String buildTable(List<T> dtoList, TableBuilder<T> tableBuilder) {
        return tableBuilder.buildTable(dtoList);
    }
}
