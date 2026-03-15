package exceptions;

import java.time.LocalTime;

public class HoraireInvalideException extends CampusException {

    private final LocalTime heureDebut;
    private final LocalTime heureFin;

    public HoraireInvalideException(LocalTime heureDebut, LocalTime heureFin) {
        super(
            "Horaire invalide : fin (" + heureFin + ") doit être après début (" + heureDebut + ").",
            "HORAIRE_INVALIDE"
        );
        this.heureDebut = heureDebut;
        this.heureFin   = heureFin;
    }

    public LocalTime getHeureDebut() { return heureDebut; }
    public LocalTime getHeureFin()   { return heureFin; }
}
