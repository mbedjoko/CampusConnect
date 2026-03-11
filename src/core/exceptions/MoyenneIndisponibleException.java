
//si un etudiant essaie de calculer une moyenne sans aucune note
public class MoyenneIndisponibleException extends Exception {
    public MoyenneIndisponibleException(String message) {
        super(message);
    }
}