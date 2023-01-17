package ua.com.foxstudent102052.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxstudent102052.model.entity.Student;

public class StudentDaoMapper {
    private StudentDaoMapper() {
    }

    public static Student mapToStudent(ResultSet studentsResultSet) throws SQLException {
        return Student.builder()
                .id(studentsResultSet.getInt(1))
                .groupId(studentsResultSet.getInt(2))
                .firstName(studentsResultSet.getString(3))
                .lastName(studentsResultSet.getString(4))
                .build();
    }

    public static List<Student> mapToStudents(ResultSet studentsResultSet) throws SQLException {
        var students = new ArrayList<Student>();

        while (studentsResultSet.next()) {
            students.add(mapToStudent(studentsResultSet));
        }
        return students;
    }
}
