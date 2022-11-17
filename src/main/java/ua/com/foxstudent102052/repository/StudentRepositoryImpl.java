package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exception.DAOException;

import java.util.List;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {
    private final DAOFactory daoFactory;

    public StudentRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }


    @Override
    public void addStudent(Student student) {
        daoFactory.doPost(String.format("""
            INSERT INTO students (
                group_id,
                first_name,
                last_name)
            VALUES ('%d', '%s', '%s');""", student.getGroupId(), student.getFirstName(), student.getLastName()));
        log.info(String.format("Student %s %s was added to the database", student.getFirstName(), student.getLastName()));
    }

    @Override
    public void removeStudent(int studentId) {
        daoFactory.doPost(String.format("""
            DELETE
            FROM students
            WHERE student_id = %d;""", studentId));
        log.info(String.format("Student with studentId %d was removed from the database", studentId));
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        daoFactory.doPost(String.format("""
            INSERT INTO students_courses (
                student_id,
                course_id)
            VALUES (%d, %d);""", studentId, courseId));
        log.info(String.format("Course with id %d was added to student with id %d", courseId, studentId));

    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        daoFactory.doPost(String.format("""
            DELETE
            FROM students_courses
            WHERE student_id = %d
            AND course_id = %d;""", studentId, courseId));
        log.info(String.format("Course with id %d was removed from student with id %d", courseId, studentId));
    }


    @Override
    public List<Student> getAllStudents() {
        String query = "SELECT * FROM students;";

        return daoFactory.getStudents(query);
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
        } catch (DAOException e) {
            log.error("Error while getting students by course id", e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Student> getStudentsByCourseNameAndGroupId(String studentName, Integer courseId) {
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
        } catch (DAOException e) {
            log.error("Error while getting students by course id", e);
            throw new DAOException(e.getMessage());
        }
    }
}
