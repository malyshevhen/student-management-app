package ua.com.foxstudent102052.repository.exception;

public class DAOException extends RuntimeException {
    private String message;
    
    public DAOException(String message) {
        super(message);
        this.message = message;
    }
}

