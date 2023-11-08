package pl.edu.pw.mwotest.exceptions;

public class ValidationFailedException extends RuntimeException{
    public ValidationFailedException(String message) {
        super(message);
    }
}
