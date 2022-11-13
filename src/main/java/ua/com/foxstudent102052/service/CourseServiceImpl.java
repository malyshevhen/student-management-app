package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.CourseRepository;

import java.util.List;

@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final String COURSE_ID_DOES_NOT_EXIST = "Course with id %d doesn't exist";
    private final CourseRepository courseRepository;

    @Override
    public void addCourse(Course course) {
        Course courseFromDB = courseRepository.getCourseByName(course.getCourseName());

        if (courseFromDB.getCourseId() == 0) {
            courseRepository.addCourse(course);

        } else {
            throw new IllegalArgumentException(
                    String.format("Course with name %s already exists", course.getCourseName()));
        }
    }

    @Override
    public void removeCourse(int id) {
        Course courseFromDB = courseRepository.getCourseById(id);

        if (courseFromDB.getCourseId() != 0) {
            courseRepository.removeCourse(id);

        } else {
            throw new IllegalArgumentException(String.format(COURSE_ID_DOES_NOT_EXIST, id));
        }
    }

    @Override
    public void updateCourse(Course course) {
        Course courseFromDB = courseRepository.getCourseById(course.getCourseId());

        if (courseFromDB.getCourseId() != 0) {
            courseRepository.updateCourse(course);

        } else {
            throw new IllegalArgumentException(String.format(COURSE_ID_DOES_NOT_EXIST, course.getCourseId()));
        }
    }

    @Override
    public void updateCourseName(int courseId, String courseName) {
        Course courseFromDB = courseRepository.getCourseById(courseId);

        if (courseFromDB.getCourseId() != 0) {
            courseRepository.updateCourseName(courseId, courseName);

        } else {
            throw new IllegalArgumentException(String.format(COURSE_ID_DOES_NOT_EXIST, courseId));
        }
    }

    @Override
    public void updateCourseDescription(int courseId, String courseDescription) {
        Course courseFromDB = courseRepository.getCourseById(courseId);

        if (courseFromDB.getCourseId() != 0) {
            courseRepository.updateCourseDescription(courseId, courseDescription);

        } else {
            throw new IllegalArgumentException(String.format(COURSE_ID_DOES_NOT_EXIST, courseId));
        }
    }

    @Override
    public List<Course> getAllCourses() {
        var allCourses = courseRepository.getAllCourses();

        if (allCourses.isEmpty()) {
            throw new IllegalArgumentException("There are no courses in database");

        } else {
            return allCourses;
        }
    }

    @Override
    public Course getCourseById(int id) {
        Course courseById = courseRepository.getCourseById(id);

        if (courseById.getCourseId() != 0) {
            return courseById;

        } else {
            throw new IllegalArgumentException(String.format(COURSE_ID_DOES_NOT_EXIST, id));
        }
    }

    @Override
    public Course getCourseByName(String name) {
        Course courseByName = courseRepository.getCourseByName(name);

        if (courseByName.getCourseId() != 0) {
            return courseByName;

        } else {
            throw new IllegalArgumentException(String.format("Course with name %s doesn't exist", name));
        }
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) {
        var studentsByCourseId = courseRepository.getStudentsByCourseId(courseId);

        if (studentsByCourseId.isEmpty()) {
            throw new IllegalArgumentException(String.format("There are no students in course with id %d", courseId));

        } else {
            return studentsByCourseId;
        }
    }
}
