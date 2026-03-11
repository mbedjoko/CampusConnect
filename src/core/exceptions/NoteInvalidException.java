package core.exceptions;

//si une note est [<0 ou >20]
public class NoteInvalidException extends Exception {
    public NoteInvalidException(String message) {
        super(message);
    }
}