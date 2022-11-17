package ua.com.foxstudent102052.service.exception;

public class NoSuchGroupExistsException extends RuntimeException {
    private final String message;

    public NoSuchGroupExistsException(String message) {
        super(message);
        this.message = message;
    }
}
