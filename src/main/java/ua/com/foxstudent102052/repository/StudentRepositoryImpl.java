package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Student;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {
    private final DAOFactory daoFactory;

    public StudentRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }


    @Override
    public void addStudent(Student student){
        try {
            daoFactory.doPost(String.format("""
                INSERT INTO students (
                    group_id,
                    first_name,
                    last_name)
                VALUES ('%d', '%s', '%s');""", student.getGroupId(), student.getFirstName(), student.getLastName()));
            log.info(String.format("Student %s %s was added to the database",
                student.getFirstName(), student.getLastName()));
        } catch (SQLException e) {
            String msg = "Error while adding student to the database";
            log.error(msg, e);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public void removeStudent(int studentId) {
        try {
            daoFactory.doPost(String.format("""
                DELETE
                FROM students
                WHERE student_id = %d;""", studentId));
            log.info(String.format("Student with studentId %d was removed from the database", studentId));
        } catch (SQLException e) {
            String msg = String.format("Student with studentId %d was not removed from the database", studentId);
            log.error(msg);

            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        try {
            daoFactory.doPost(String.format("""
                INSERT INTO students_courses (
                    student_id,
                    course_id)
                VALUES (%d, %d);""", studentId, courseId));
            log.info(String.format("Course with id %d was added to student with id %d", courseId, studentId));
        } catch (SQLException e) {
            String msg = String.format("Student with id %d was not added to Course with id %d", studentId, courseId);
            log.error(msg);

            throw new IllegalArgumentException(msg, e);
        }

    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId){
        try {
            daoFactory.doPost(String.format("""
                DELETE
                FROM students_courses
                WHERE student_id = %d
                AND course_id = %d;""", studentId, courseId));
            log.info(String.format("Course with id %d was removed from student with id %d", courseId, studentId));
        } catch (SQLException e) {
            String msg = String.format("Student with id %d was not removed from course with id %d", studentId, courseId);
            log.error(msg);

            throw new IllegalArgumentException(msg, e);
        }
    }


    @Override
    public List<Student> getAllStudents() {
        String query = "SELECT * FROM students;";
        try {
            return daoFactory.getStudents(query);
        } catch (SQLException e) {
            String msg = "There are no students in the database";
            log.error(msg, e);
            
            throw new IllegalArgumentException(msg, e);
        }

    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        String query = String.format("""
                SELECT *
                FROM students
                WHERE student_id IN (
                    SELECT student_id
                    FROM students_courses
                    WHERE course_id = %d);""",
            courseId);

        try {
            return daoFactory.getStudents(query);
        } catch (SQLException e) {
            String msg = "Error while getting students by course id";
            log.error(msg, e);
            
            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByNameAndCourse(String studentName, Integer courseId) {
        String query = String.format("""
                SELECT *
                FROM students
                WHERE student_id IN (
                    SELECT student_id
                    FROM students_courses
                    WHERE course_id = %d)
                AND first_name ='%s';""",
            courseId, studentName);

        try {
            return daoFactory.getStudents(query);
        } catch (SQLException e) {
            String msg = "Error while getting students by name and course";
            log.error(msg, e);
            
            throw new IllegalArgumentException(msg, e);
        }
    }
}
