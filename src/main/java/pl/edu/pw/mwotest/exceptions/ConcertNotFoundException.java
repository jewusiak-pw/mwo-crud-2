package pl.edu.pw.mwotest.exceptions;

public class ConcertNotFoundException extends RuntimeException{
    public ConcertNotFoundException() {
        super("Concert not found");
    }
}
