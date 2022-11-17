package ua.com.foxstudent102052.service.exception;

public class GroupAlreadyExistException extends RuntimeException {
    private final String msg;

    public GroupAlreadyExistException(String message) {
        super(message);
        this.msg = message;
    }
}
