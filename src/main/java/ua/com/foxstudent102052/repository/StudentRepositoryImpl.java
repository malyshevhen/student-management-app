package ua.com.foxstudent102052.repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;

import java.util.List;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {
    DAOFactory daoFactory = DAOFactoryImpl.getInstance();
    private static StudentRepository instance;

    private StudentRepositoryImpl() {
    }

    public static StudentRepository getInstance() {
        if (instance == null) {
            instance = new StudentRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void addStudent(@NonNull Student student) {
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
    public void updateStudentFirstName(int studentId, String firstName) {
        daoFactory.doPost(String.format("""
            UPDATE students
            SET first_name = '%s'
            WHERE student_id = %d;""", firstName, studentId));
        log.info(String.format("Student with id %d was updated with first name %s", studentId, firstName));
    }

    @Override
    public void updateStudentLastName(int studentId, String lastName) {
        daoFactory.doPost(String.format("""
            UPDATE students
            SET last_name = '%s'
            WHERE student_id = %d;""", lastName, studentId));
        log.info(String.format("Student with id %d was updated with last name %s", studentId, lastName));
    }

    @Override
    public void updateStudentGroup(int studentId, int groupId) {
        daoFactory.doPost(String.format("""
            UPDATE students
            SET group_id = %d
            WHERE student_id = %d;""", groupId, studentId));
        log.info(String.format("Student with id %d was updated with group id %d", studentId, groupId));
    }

    @Override
    public void updateStudent(@NonNull Student student) {
        daoFactory.doPost(String.format("""
            UPDATE students
            SET
                group_id = %d,
                first_name = '%s',
                last_name = '%s'
            WHERE student_id = %d;""", student.getGroupId(), student.getFirstName(), student.getLastName(), student.getStudentId()));
        log.info(String.format("Student with id %d was updated with group id %d, first name %s and last name %s",
            student.getStudentId(),
            student.getGroupId(),
            student.getFirstName(),
            student.getLastName()));
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
    public Student getStudentById(int studentId) {
        String query = String.format("""
            SELECT *
            FROM students
            WHERE student_id = %d;""", studentId);

        return daoFactory.getStudent(query);
    }


    @Override
    public List<Student> getAllStudents() {
        String query = "SELECT * FROM students;";

        return daoFactory.getStudents(query);
    }

    @Override
    public List<Student> getStudentsByFirstName(String firstName) {
        String query = String.format("""
            SELECT *
            FROM students
            WHERE first_name = '%s';""", firstName);

        return daoFactory.getStudents(query);
    }

    @Override
    public List<Student> getStudentsByLastName(String lastName) {
        String query = String.format("""
            SELECT *
            FROM students
            WHERE last_name = '%s';""", lastName);

        return daoFactory.getStudents(query);
    }

    @Override
    public List<Student> getStudentsByFullName(String firstName, String lastName) {
        String query = String.format("""
            SELECT *
            FROM students
            WHERE first_name = '%s'
            AND last_name = '%s';""", firstName, lastName);

        return daoFactory.getStudents(query);
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) {
        String query = String.format("""
            SELECT *
            FROM courses
            WHERE course_id
            IN (
                SELECT course_id
                FROM students_courses
                WHERE student_id = %d);""", studentId);

        return daoFactory.getCourses(query);
    }
}
