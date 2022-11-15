package ua.com.foxstudent102052.service;

import lombok.AllArgsConstructor;
import ua.com.foxstudent102052.model.Course;
import ua.com.foxstudent102052.model.Student;
import ua.com.foxstudent102052.repository.StudentRepository;

import java.util.List;

@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String STUDENT_WITH_ID_NOT_EXIST = "Student with id %d doesn't exist";
    private final StudentRepository studentRepository;

    @Override
    public void addStudent(Student student) {
        int studentId = student.getStudentId();
        
        if (Boolean.FALSE.equals(ifExists(studentId))) {
            studentRepository.addStudent(student);

        } else {
            throw new IllegalArgumentException(String.format("Student with id %d already exist", studentId));
        }
    }

    @Override
    public void removeStudent(int studentId) {

        if (Boolean.TRUE.equals(ifExists(studentId))) {
            studentRepository.removeStudent(studentId);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }
    }

    @Override
    public void updateStudentFirstName(int studentId, String firstName) {
        Student studentForUpdate = studentRepository.getStudentById(studentId);
        
        if (studentForUpdate != null) {
            studentForUpdate.setFirstName(firstName);
            studentRepository.updateStudent(studentForUpdate);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }
    }

    @Override
    public void updateStudentLastName(int studentId, String lastName) {
        Student studentForUpdate = studentRepository.getStudentById(studentId);
        
        if (studentForUpdate != null) {
            studentForUpdate.setLastName(lastName);
            studentRepository.updateStudent(studentForUpdate);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }
    }

    @Override
    public void updateStudentGroup(int studentId, int groupId) {
        Student studentForUpdate = studentRepository.getStudentById(studentId);
        
        if (studentForUpdate != null) {
            studentForUpdate.setGroupId(groupId);
            studentRepository.updateStudent(studentForUpdate);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }
    }

    @Override
    public void updateStudent(Student student) {

        if (studentRepository.getStudentById(student.getStudentId()).getStudentId() != 0) {
            studentRepository.updateStudent(student);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, student.getStudentId()));
        }
    }

    @Override
    public List<Student> getAllStudents() {

        if (!studentRepository.getAllStudents().isEmpty()) {
            return studentRepository.getAllStudents();

        } else {
            throw new IllegalArgumentException("There are no students in the database");
        }
    }

    @Override
    public Student getStudentById(int studentId) {

        if (Boolean.TRUE.equals(ifExists(studentId))) {
            return studentRepository.getStudentById(studentId);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }
    }

    @Override
    public List<Student> getStudentsByLastName(String lastName) {

        if (!studentRepository.getStudentsByLastName(lastName).isEmpty()) {
            return studentRepository.getStudentsByLastName(lastName);

        } else {
            throw new IllegalArgumentException(String.format(
                "There are no students with last name %s in the database", lastName));
        }
    }

    @Override
    public List<Student> getStudentsByName(String name) {

        if (!studentRepository.getStudentsByFirstName(name).isEmpty()) {
            return studentRepository.getStudentsByFirstName(name);

        } else {
            throw new IllegalArgumentException(String.format(
                "There are no students with name %s in the database", name));
        }
    }

    @Override
    public List<Student> getStudentsByFullName(String firstName, String lastName) {

        if (!studentRepository.getStudentsByFullName(firstName, lastName).isEmpty()) {
            return studentRepository.getStudentsByFullName(firstName, lastName);

        } else {
            throw new IllegalArgumentException(String.format(
                "There are no students with name %s and last name %s in the database", firstName, lastName));
        }
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {

        if (studentRepository.getStudentById(studentId).getStudentId() != 0) {
            studentRepository.addStudentToCourse(studentId, courseId);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int groupId) {

        if (studentRepository.getStudentById(studentId).getStudentId() != 0) {
            studentRepository.removeStudentFromCourse(studentId, groupId);

        } else {
            throw new IllegalArgumentException(String.format(STUDENT_WITH_ID_NOT_EXIST, studentId));
        }

    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) {

        if (!studentRepository.getCoursesByStudentId(studentId).isEmpty()) {
            return studentRepository.getCoursesByStudentId(studentId);

        } else {
            throw new IllegalArgumentException(String.format("Student with id %d doesn't have any courses", studentId));
        }
    }
    
    private Boolean ifExists(int id){
        return studentRepository.getStudentById(id) != null;
    }
}
