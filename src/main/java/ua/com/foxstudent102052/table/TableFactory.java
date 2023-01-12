package ua.com.foxstudent102052.table;

import org.springframework.stereotype.Component;
import ua.com.foxstudent102052.table.interfaces.TableBuilder;

import java.util.List;

@Component
public class TableFactory {
    public <T> String buildTable(List<T> dtoList, TableBuilder<T> tableBuilder) {
        return tableBuilder.buildTable(dtoList);
    }
}
