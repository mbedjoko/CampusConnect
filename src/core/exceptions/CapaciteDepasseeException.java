package core.exceptions;

public class CapaciteDepasseeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CapaciteDepasseeException(String message) {
        super(message);
    }
}