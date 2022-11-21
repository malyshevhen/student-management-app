package ua.com.foxstudent102052.repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;

@Slf4j
public class CourseRepositoryImpl implements CourseRepository {
    private final DAOFactory daoFactory;

    public CourseRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addCourse(Course course) throws RepositoryException {
        try {
            daoFactory.doPost(String.format("""
                    INSERT
                    INTO courses (course_name, course_description)
                    VALUES ('%s', '%s');""",
                    course.getCourseName(),
                    course.getCourseDescription()));
        } catch (DAOException e) {
            log.error("Error while adding course", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Course> getAllCourses() throws RepositoryException {
        String query = "SELECT * FROM courses;";

        try {
            return daoFactory.getCourses(query);
        } catch (DAOException e) {
            log.error("Error while getting all courses", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public Course getCourseById(int courseId) throws RepositoryException {
        String query = String.format("""
                SELECT *
                FROM courses
                WHERE course_id = %d;""",
                courseId);

        try {
            return daoFactory.getCourse(query);
        } catch (DAOException e) {
            log.error("Error while getting course by id", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) throws RepositoryException {
        try {
            String query = String.format("""
                    SELECT *
                    FROM courses
                    WHERE course_id
                    IN (
                        SELECT course_id
                        FROM students_courses
                        WHERE student_id = %d);""", studentId);

            return daoFactory.getCourses(query);
        } catch (DAOException e) {
            log.error("Error while getting courses by student id", e);

            throw new RepositoryException(e);
        }
    }

    @Override
    public Course getLastCourse() throws RepositoryException {
        try {
            return daoFactory.getCourse(
                    """
                            SELECT *
                            FROM courses
                            WHERE course_id = (
                                SELECT MAX(course_id)
                                FROM courses);""");

        } catch (DAOException e) {
            log.error("Error while getting last course", e);

            throw new RepositoryException(e);
        }
    }
}
