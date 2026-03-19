package exceptions;

/**
 * Exception levée lorsqu'une note est hors de l'intervalle [0, 20]
 * ou qu'un coefficient est nul ou négatif.
 *
 * @author Responsable Exceptions & Validations
 * @version 1.0
 */
public class NoteInvalideException extends CampusException {

    private final double valeurFournie;

    /**
     * Constructeur pour une note hors-plage [0, 20].
     *
     * @param valeurFournie La valeur de note invalide
     */
    public NoteInvalideException(double valeurFournie) {
        super(
            "Note invalide : " + valeurFournie + ". La note doit être comprise entre 0 et 20.",
            "NOTE_INVALIDE"
        );
        this.valeurFournie = valeurFournie;
    }

    /**
     * Constructeur pour un champ invalide avec contexte (ex: coefficient).
     *
     * @param valeurFournie La valeur invalide
     * @param contexte      Nom du champ concerné (ex: "coefficient")
     */
    public NoteInvalideException(double valeurFournie, String contexte) {
        super(
            "Valeur invalide pour '" + contexte + "' : " + valeurFournie
            + ". Le coefficient doit être strictement positif.",
            "NOTE_INVALIDE"
        );
        this.valeurFournie = valeurFournie;
    }

    /**
     * Retourne la valeur invalide qui a provoqué l'exception.
     *
     * @return la valeur fournie
     */
    public double getValeurFournie() {
        return valeurFournie;
    }
}
