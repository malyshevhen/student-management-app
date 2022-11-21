package ua.com.foxstudent102052.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;

@Slf4j
public class CourseRepositoryImpl implements CourseRepository {
    private static final String QUERY_EXECUTED_SUCCESSFULLY = "Query executed successfully";

    private final DAOFactory daoFactory;

    public CourseRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addCourse(Course course) throws RepositoryException {
        var query = String.format("""
            INSERT
            INTO courses (course_name, course_description)
            VALUES ('%s', '%s');""",
            course.getCourseName(),
            course.getCourseDescription());

        try {
            daoFactory.doPost(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

        } catch (DAOException e) {
            log.error("Error while adding course", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Course> getAllCourses() throws RepositoryException {
        var query = "SELECT * FROM courses;";

        try {
            var courses = daoFactory.getCourses(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return courses;

        } catch (DAOException e) {
            log.error("Error while getting all courses", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public Course getCourseById(int courseId) throws RepositoryException {
        var query = String.format("""
                SELECT *
                FROM courses
                WHERE course_id = %d;""",
                courseId);

        try {
            var course = daoFactory.getCourse(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return course;

        } catch (DAOException e) {
            log.error("Error while getting course by id", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) throws RepositoryException {
        var query = String.format("""
            SELECT *
            FROM courses
            WHERE course_id
            IN (
                SELECT course_id
                FROM students_courses
                WHERE student_id = %d);""", studentId);

        try {
            var courses = daoFactory.getCourses(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return courses;

        } catch (DAOException e) {
            log.error("Error while getting courses by student id", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public Course getLastCourse() throws RepositoryException {
        var query = """
            SELECT *
            FROM courses
            WHERE course_id = (
                SELECT MAX(course_id)
                FROM courses);""";

        try {
            var course = daoFactory.getCourse(query);
            log.info(QUERY_EXECUTED_SUCCESSFULLY);

            return course;

        } catch (DAOException e) {
            log.error("Error while getting last course", e);

            throw new RepositoryException(e);
        }
    }
}
