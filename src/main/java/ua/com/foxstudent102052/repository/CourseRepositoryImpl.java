package ua.com.foxstudent102052.repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.exception.DAOException;

import java.util.List;

@Slf4j
public class CourseRepositoryImpl implements CourseRepository {
    DAOFactory daoFactory;
    
    public CourseRepositoryImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    @Override
    public void addCourse(@NonNull Course course) {
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
    public void removeCourse(int id) {
        try{
            daoFactory.doPost(String.format("""
                    DELETE
                    FROM courses
                    WHERE course_id = %d;""",
                id));
        } catch (DAOException e) {
            log.error("Error while removing course", e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void updateCourse(@NonNull Course course) {
        try{
            daoFactory.doPost(String.format("""
                    UPDATE courses
                    SET (course_name, course_description) = ('%s', '%s')
                    WHERE course_id = %d;""",
                course.getCourseName(),
                course.getCourseDescription(),
                course.getCourseId()));
        } catch (DAOException e) {
            log.error("Error while updating course", e);
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
    public Course getCourseByName(String courseName) {
        String query = String.format("""
                SELECT *
                FROM courses
                WHERE course_name = '%s';""",
            courseName);

        try {
            return daoFactory.getCourse(query);
        } catch (DAOException e) {
            log.error("Error while getting course by name", e);
            throw new DAOException(e.getMessage());
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
        } catch (DAOException e) {
            log.error("Error while getting students by course id", e);
            throw new DAOException(e.getMessage());
        }
    }
}
