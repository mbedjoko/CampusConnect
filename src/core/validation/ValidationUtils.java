package validation;

import exceptions.CapaciteDepasseeException;
import exceptions.HoraireInvalideException;
import exceptions.NoteInvalideException;

import java.time.LocalTime;

/**
 * Classe utilitaire statique centralisant toutes les validations du système CampusConnect.
 *
 * <p>Cette classe ne peut pas être instanciée. Elle regroupe les vérifications
 * des règles métier suivantes :</p>
 * <ul>
 *   <li>Notes comprises entre 0 et 20</li>
 *   <li>Coefficients strictement positifs</li>
 *   <li>Heure de fin strictement après heure de début</li>
 *   <li>Capacité de groupe ou de salle non dépassée</li>
 * </ul>
 *
 * <p><strong>Utilisation :</strong></p>
 * <pre>
 *   ValidationUtils.validerNote(15.5);       // OK
 *   ValidationUtils.validerNote(-1);         // lève NoteInvalideException
 *   ValidationUtils.validerHoraire(h1, h2);  // lève HoraireInvalideException si h2 <= h1
 * </pre>
 *
 * @author Responsable Exceptions & Validations
 * @version 1.0
 */
public final class ValidationUtils {

    /** Empêche l'instanciation. */
    private ValidationUtils() {
        throw new UnsupportedOperationException("Classe utilitaire, non instanciable.");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  VALIDATION DES NOTES
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Vérifie qu'une note est dans l'intervalle [0, 20].
     *
     * @param note La valeur à vérifier
     * @throws NoteInvalideException si note < 0 ou note > 20
     */
    public static void validerNote(double note) throws NoteInvalideException {
        if (note < 0 || note > 20) {
            throw new NoteInvalideException(note);
        }
    }

    /**
     * Vérifie qu'un coefficient est strictement positif.
     *
     * @param coefficient La valeur à vérifier
     * @throws NoteInvalideException si coefficient <= 0
     */
    public static void validerCoefficient(double coefficient) throws NoteInvalideException {
        if (coefficient <= 0) {
            throw new NoteInvalideException(coefficient, "coefficient");
        }
    }

    /**
     * Valide une note ET son coefficient en une seule opération.
     *
     * @param note        La note (doit être dans [0, 20])
     * @param coefficient Le coefficient (doit être > 0)
     * @throws NoteInvalideException si l'une des deux valeurs est invalide
     */
    public static void validerNoteEtCoefficient(double note, double coefficient)
            throws NoteInvalideException {
        validerNote(note);
        validerCoefficient(coefficient);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  VALIDATION DES HORAIRES
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Vérifie que l'heure de fin est strictement après l'heure de début.
     *
     * @param heureDebut Heure de début
     * @param heureFin   Heure de fin
     * @throws HoraireInvalideException si heureFin <= heureDebut ou si l'une est null
     */
    public static void validerHoraire(LocalTime heureDebut, LocalTime heureFin)
            throws HoraireInvalideException {
        if (heureDebut == null || heureFin == null || !heureFin.isAfter(heureDebut)) {
            throw new HoraireInvalideException(heureDebut, heureFin);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  VALIDATION DES CAPACITÉS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Vérifie que le nombre d'éléments actuel ne dépasse pas la capacité maximale.
     *
     * @param nomElement   Nom de l'élément à vérifier (ex: "Groupe TD1", "Salle 402")
     * @param nombreActuel Nombre actuel d'inscrits / occupants
     * @param capaciteMax  Capacité maximale autorisée
     * @throws CapaciteDepasseeException si nombreActuel >= capaciteMax
     */
    public static void validerCapacite(String nomElement, int nombreActuel, int capaciteMax)
            throws CapaciteDepasseeException {
        if (nombreActuel >= capaciteMax) {
            throw new CapaciteDepasseeException(nomElement, capaciteMax, nombreActuel);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  VALIDATIONS GÉNÉRALES
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Vérifie qu'une chaîne n'est pas nulle ou vide.
     *
     * @param valeur   La chaîne à vérifier
     * @param nomChamp Nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si la chaîne est nulle ou vide
     */
    public static void validerChaineNonVide(String valeur, String nomChamp) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException(
                "Le champ '" + nomChamp + "' ne peut pas être vide."
            );
        }
    }

    /**
     * Vérifie qu'un entier est strictement positif.
     *
     * @param valeur   La valeur à vérifier
     * @param nomChamp Nom du champ (pour le message d'erreur)
     * @throws IllegalArgumentException si valeur <= 0
     */
    public static void validerEntierPositif(int valeur, String nomChamp) {
        if (valeur <= 0) {
            throw new IllegalArgumentException(
                "Le champ '" + nomChamp + "' doit être > 0. Valeur reçue : " + valeur
            );
        }
    }
}
