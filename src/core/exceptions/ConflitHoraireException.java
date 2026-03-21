package core.exceptions;

public class ConflitHoraireException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConflitHoraireException(String message) {
        super(message);
    }
}