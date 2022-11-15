package ua.com.foxstudent102052.service.exception;

public class CourseAlreadyExistException extends RuntimeException {
    private String message;

    public CourseAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
