package ua.com.foxstudent102052.service.exception;

public class StudentAlreadyExistException extends RuntimeException {
    private final String message;

    public StudentAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
