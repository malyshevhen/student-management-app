package ua.com.foxstudent102052.service.exception;

public class NoSuchCourseExistsException extends RuntimeException {
    private String message;

    public NoSuchCourseExistsException(String message) {
        super(message);
        this.message = message;
    }
}
