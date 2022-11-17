package ua.com.foxstudent102052.service.exception;

public class NoSuchStudentExistsException extends RuntimeException {
    private final String message;

    public NoSuchStudentExistsException(String message) {
        super(message);
        this.message = message;
    }
}
