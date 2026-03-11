package core.exceptions;

//si une note est [<0 ou >20]
public class NoteInvalidException extends Exception {
    public NoteInvalidException(String message) {
        super(message);
    }
}

//si un etudiant essaie de calculer une moyenne sans aucune note
public class MoyenneIndisponibleException extends Exception {
    public MoyenneIndisponibleException(String message) {
        super(message);
    }
}