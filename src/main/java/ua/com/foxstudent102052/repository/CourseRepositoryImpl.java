package ua.com.foxstudent102052.repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class CourseRepositoryImpl implements CourseRepository {
    private final DAOFactory daoFactory;
    
    public CourseRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    @Override
    public void addCourse(Course course) throws IllegalArgumentException {
        try{
            daoFactory.doPost(String.format("""
                    INSERT
                    INTO courses (course_name, course_description)
                    VALUES ('%s', '%s');""",
                course.getCourseName(),
                course.getCourseDescription()));
        } catch (SQLException e) {
            log.error("Error while adding course", e);
            
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<Course> getAllCourses() throws NoSuchElementException {
        String query = "SELECT * FROM courses;";

        try {
            return daoFactory.getCourses(query);
        } catch (SQLException e) {
            log.error("Error while getting all courses", e);
            
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Course getCourseById(int courseId) throws NoSuchElementException {
        String query = String.format("""
                SELECT *
                FROM courses
                WHERE course_id = %d;""",
            courseId);

        try {
            return daoFactory.getCourse(query);
        } catch (SQLException e) {
            log.error("Error while getting course by id", e);
            
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) throws NoSuchElementException {
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
        } catch (SQLException e) {
            log.error("Error while getting courses by student id", e);
            
            throw new NoSuchElementException(e);
        }
    }
}
