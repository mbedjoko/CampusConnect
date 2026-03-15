package validation;

import exceptions.CapaciteDepasseeException;
import exceptions.HoraireInvalideException;
import exceptions.NoteInvalideException;

import java.time.LocalTime;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void validerNote(double note) throws NoteInvalideException {
        if (note < 0 || note > 20) throw new NoteInvalideException(note);
    }

    public static void validerCoefficient(double coeff) throws NoteInvalideException {
        if (coeff <= 0) throw new NoteInvalideException(coeff, "coefficient");
    }

    public static void validerNoteEtCoefficient(double note, double coeff) throws NoteInvalideException {
        validerNote(note);
        validerCoefficient(coeff);
    }

    public static void validerHoraire(LocalTime debut, LocalTime fin) throws HoraireInvalideException {
        if (debut == null || fin == null || !fin.isAfter(debut))
            throw new HoraireInvalideException(debut, fin);
    }

    public static void validerCapacite(String element, int actuel, int max) throws CapaciteDepasseeException {
        if (actuel >= max) throw new CapaciteDepasseeException(element, max, actuel);
    }

    public static void validerChaineNonVide(String valeur, String champ) {
        if (valeur == null || valeur.isBlank())
            throw new IllegalArgumentException("Champ '" + champ + "' vide ou null.");
    }

    public static void validerEntierPositif(int valeur, String champ) {
        if (valeur <= 0)
            throw new IllegalArgumentException("Champ '" + champ + "' doit être > 0. Reçu : " + valeur);
    }
}
