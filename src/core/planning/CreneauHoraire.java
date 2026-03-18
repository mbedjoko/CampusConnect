package core.planning;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public final class CreneauHoraire {
    private final LocalDate date;
    private final LocalTime heureDebut;
    private final LocalTime heureFin;

    public CreneauHoraire(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.date = Objects.requireNonNull(date, "date");
        this.heureDebut = Objects.requireNonNull(heureDebut, "heureDebut");
        this.heureFin = Objects.requireNonNull(heureFin, "heureFin");
        if (!heureDebut.isBefore(heureFin)) {
            throw new IllegalArgumentException("heureDebut doit être strictement avant heureFin");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public boolean chevauche(CreneauHoraire other) {
        Objects.requireNonNull(other, "other");
        if (!date.equals(other.date)) {
            return false;
        }
        return heureDebut.isBefore(other.heureFin) && other.heureDebut.isBefore(heureFin);
    }
}
