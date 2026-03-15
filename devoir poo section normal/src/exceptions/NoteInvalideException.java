package exceptions;

public class NoteInvalideException extends CampusException {

    private final double valeurFournie;

    public NoteInvalideException(double valeurFournie) {
        super("Note invalide : " + valeurFournie + " (intervalle attendu : [0, 20]).", "NOTE_INVALIDE");
        this.valeurFournie = valeurFournie;
    }

    public NoteInvalideException(double valeurFournie, String champ) {
        super("Valeur invalide pour '" + champ + "' : " + valeurFournie + " (doit être > 0).", "NOTE_INVALIDE");
        this.valeurFournie = valeurFournie;
    }

    public double getValeurFournie() { return valeurFournie; }
}
