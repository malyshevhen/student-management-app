package ua.com.foxstudent102052.service.exceptions;

public class ElementAlreadyExistException extends RuntimeException {
    public ElementAlreadyExistException(String message) {
        super(message);
    }
}
