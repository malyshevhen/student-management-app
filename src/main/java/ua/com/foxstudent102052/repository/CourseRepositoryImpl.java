package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.repository.exception.DAOException;

import java.util.List;

@Slf4j
public class CourseRepositoryImpl implements CourseRepository {
    private final DAOFactory daoFactory;
    
    public CourseRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    @Override
    public void addCourse(Course course) {
        try{
            daoFactory.doPost(String.format("""
                    INSERT
                    INTO courses (course_name, course_description)
                    VALUES ('%s', '%s');""",
                course.getCourseName(),
                course.getCourseDescription()));
        } catch (DAOException e) {
            log.error("Error while adding course", e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Course> getAllCourses() {
        String query = "SELECT * FROM courses;";

        try {
            return daoFactory.getCourses(query);
        } catch (DAOException e) {
            log.error("Error while getting all courses", e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        String query = String.format("""
                SELECT *
                FROM courses
                WHERE course_id = %d;""",
            courseId);

        try {
            return daoFactory.getCourse(query);
        } catch (DAOException e) {
            log.error("Error while getting course by id", e);
            throw new DAOException(e.getMessage());
        }
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
