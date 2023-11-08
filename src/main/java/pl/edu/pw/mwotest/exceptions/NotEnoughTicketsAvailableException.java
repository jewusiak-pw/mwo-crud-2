package pl.edu.pw.mwotest.exceptions;

public class NotEnoughTicketsAvailableException extends RuntimeException {
    public NotEnoughTicketsAvailableException() {
        super("Not enough tickets available");
    }
}
