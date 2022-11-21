package ua.com.foxstudent102052.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Student;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {
    private static final String QUERY_EXECUTED_SUCCESSFULLY = "Query executed successfully";

    private final DAOFactory daoFactory;

    public StudentRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addStudent(Student student) throws RepositoryException {
        var query = String.format(
                """
                        INSERT INTO students (
                            group_id,
                            first_name,
                            last_name)
                        VALUES ('%d', '%s', '%s');""",
                student.getGroupId(), student.getFirstName(), student.getLastName());

        try {
            daoFactory.doPost(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (DAOException e) {
            var msg = "Error while adding student to the database";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public void removeStudent(int studentId) throws RepositoryException {
        var query = String.format(
                """
                        DELETE
                        FROM students
                        WHERE student_id = %d;""",
                studentId);

        try {
            daoFactory.doPost(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (DAOException e) {
            var msg = String.format("Student with studentId %d was not removed from the database", studentId);
            log.error(msg);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws RepositoryException {
        var query = String.format(
                """
                        INSERT INTO students_courses (
                            student_id,
                            course_id)
                        VALUES (%d, %d);""", studentId, courseId);

        try {
            daoFactory.doPost(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (DAOException e) {
            var msg = String.format("Student with id %d was not added to Course with id %d", studentId, courseId);
            log.error(msg);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws RepositoryException {
        var query = String.format(
                """
                        DELETE
                        FROM students_courses
                        WHERE student_id = %d
                        AND course_id = %d;""", studentId, courseId);

        try {
            daoFactory.doPost(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (DAOException e) {
            var msg = String.format("Student with id %d was not removed from course with id %d", studentId,
                    courseId);
            log.error(msg);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getAllStudents() throws RepositoryException {
        var query = "SELECT * FROM students;";

        try {
            var students = daoFactory.getStudents(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return students;

        } catch (DAOException e) {
            String msg = "There are no students in the database";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) throws RepositoryException {
        var query = String.format(
                """
                        SELECT *
                        FROM students
                        WHERE student_id IN (
                            SELECT student_id
                            FROM students_courses
                            WHERE course_id = %d);""",
                courseId);

        try {
            var students = daoFactory.getStudents(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return students;

        } catch (DAOException e) {
            var msg = "Error while getting students by course id";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByNameAndCourse(String studentName, Integer courseId) throws RepositoryException {
        var query = String.format(
                """
                        SELECT *
                        FROM students
                        WHERE student_id IN (
                            SELECT student_id
                            FROM students_courses
                            WHERE course_id = %d)
                        AND first_name ='%s';""",
                courseId, studentName);

        try {
            var students = daoFactory.getStudents(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return students;

        } catch (DAOException e) {
            var msg = "Error while getting students by name and course";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Student getStudentById(int id) throws RepositoryException {
        var query = String.format(
                """
                        SELECT *
                        FROM students
                        WHERE student_id = %d;""",
                id);

        try {
            var student = daoFactory.getStudents(query).get(0);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return student;

        } catch (DAOException e) {
            var msg = "Error while getting student by id";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Student getLastStudent() throws RepositoryException {
        var query = """
                SELECT *
                FROM students
                WHERE student_id =
                    (SELECT MAX(student_id)
                    FROM students);""";

        try {
            var student = daoFactory.getStudents(query).get(0);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return student;

        } catch (DAOException e) {
            var msg = "Error while getting last student";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }
}
