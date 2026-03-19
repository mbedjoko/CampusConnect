package exceptions;

/**
 * Classe mère abstraite de toutes les exceptions métier de CampusConnect.
 * Toutes les exceptions personnalisées héritent de cette classe.
 *
 * @author Responsable Exceptions & Validations
 * @version 1.0
 */
public abstract class CampusException extends Exception {

    private final String codeErreur;

    /**
     * Constructeur.
     *
     * @param message    Description lisible de l'erreur
     * @param codeErreur Code unique identifiant le type d'erreur
     */
    public CampusException(String message, String codeErreur) {
        super(message);
        this.codeErreur = codeErreur;
    }

    /**
     * Retourne le code d'erreur associé.
     *
     * @return le code d'erreur (ex: "CONFLIT_HORAIRE")
     */
    public String getCodeErreur() {
        return codeErreur;
    }

    @Override
    public String toString() {
        return "[" + codeErreur + "] " + getMessage();
    }
}
