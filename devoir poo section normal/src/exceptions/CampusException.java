package exceptions;

public abstract class CampusException extends Exception {

    private final String codeErreur;

    public CampusException(String message, String codeErreur) {
        super(message);
        this.codeErreur = codeErreur;
    }

    public String getCodeErreur() {
        return codeErreur;
    }

    @Override
    public String toString() {
        return "[" + codeErreur + "] " + getMessage();
    }
}
