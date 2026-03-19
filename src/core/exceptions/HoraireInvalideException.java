package exceptions;

import java.time.LocalTime;

/**
 * Exception levée lorsqu'un créneau horaire est invalide,
 * c'est-à-dire lorsque l'heure de fin est antérieure ou égale à l'heure de début.
 *
 * @author Responsable Exceptions & Validations
 * @version 1.0
 */
public class HoraireInvalideException extends CampusException {

    private final LocalTime heureDebut;
    private final LocalTime heureFin;

    /**
     * Constructeur.
     *
     * @param heureDebut Heure de début fournie
     * @param heureFin   Heure de fin fournie (doit être > heureDebut)
     */
    public HoraireInvalideException(LocalTime heureDebut, LocalTime heureFin) {
        super(
            "Horaire invalide : l'heure de fin (" + heureFin
            + ") doit être strictement après l'heure de début (" + heureDebut + ").",
            "HORAIRE_INVALIDE"
        );
        this.heureDebut = heureDebut;
        this.heureFin   = heureFin;
    }

    /**
     * Retourne l'heure de début invalide.
     *
     * @return heure de début
     */
    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    /**
     * Retourne l'heure de fin invalide.
     *
     * @return heure de fin
     */
    public LocalTime getHeureFin() {
        return heureFin;
    }
}
