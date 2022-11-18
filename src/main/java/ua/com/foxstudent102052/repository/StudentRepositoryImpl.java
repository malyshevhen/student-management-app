package ua.com.foxstudent102052.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Student;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {
    private final DAOFactory daoFactory;

    public StudentRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addStudent(Student student) throws RepositoryException {
        try {
            daoFactory.doPost(String.format("""
                    INSERT INTO students (
                        group_id,
                        first_name,
                        last_name)
                    VALUES ('%d', '%s', '%s');""", student.getGroupId(), student.getFirstName(),
                    student.getLastName()));
            log.info(String.format("Student %s %s was added to the database",
                    student.getFirstName(), student.getLastName()));
        } catch (DAOException e) {
            String msg = "Error while adding student to the database";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public void removeStudent(int studentId) throws RepositoryException {
        try {
            daoFactory.doPost(String.format("""
                    DELETE IF EXISTS
                    FROM students
                    WHERE student_id = %d;""", studentId));
            log.info(String.format("Student with studentId %d was removed from the database", studentId));
        } catch (DAOException e) {
            String msg = String.format("Student with studentId %d was not removed from the database", studentId);
            log.error(msg);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws RepositoryException {
        try {
            daoFactory.doPost(String.format("""
                    INSERT INTO students_courses (
                        student_id,
                        course_id)
                    VALUES (%d, %d);""", studentId, courseId));
            log.info(String.format("Course with id %d was added to student with id %d", courseId, studentId));
        } catch (DAOException e) {
            String msg = String.format("Student with id %d was not added to Course with id %d", studentId, courseId);
            log.error(msg);

            throw new RepositoryException(msg, e);
        }

    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws RepositoryException {
        try {
            daoFactory.doPost(String.format("""
                    DELETE IF EXISTS
                    FROM students_courses
                    WHERE student_id = %d
                    AND course_id = %d;""", studentId, courseId));
            log.info(String.format("Course with id %d was removed from student with id %d", courseId, studentId));
        } catch (DAOException e) {
            String msg = String.format("Student with id %d was not removed from course with id %d", studentId,
                    courseId);
            log.error(msg);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getAllStudents() throws RepositoryException {
        String query = "SELECT * FROM students;";
        try {
            return daoFactory.getStudents(query);
        } catch (DAOException e) {
            String msg = "There are no students in the database";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }

    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) throws RepositoryException {
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
            String msg = "Error while getting students by course id";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public List<Student> getStudentsByNameAndCourse(String studentName, Integer courseId) throws RepositoryException {
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
            String msg = "Error while getting students by name and course";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

    @Override
    public Student getStudentById(int id) throws RepositoryException {
        String query = String.format("""
                SELECT *
                FROM students
                WHERE student_id = %d;""",
                id);

        try {
            return daoFactory.getStudents(query).get(0);
        } catch (DAOException e) {
            String msg = "Error while getting student by id";
            log.error(msg, e);

            throw new RepositoryException(msg, e);
        }
    }

}
