package ua.com.foxstudent102052.table.interfaces;

import java.util.List;

public interface TableBuilder<T> {
    String buildTable(List<T> dtoList);
}
