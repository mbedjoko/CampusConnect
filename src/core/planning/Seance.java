package core.planning;

import core.actors.Enseignant;
import core.courses.Cours;
import core.group.Groupe;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Seance {
    private String id;
    private Groupe groupe;
    private Enseignant enseignant;
    private Cours cours;
    private String salle;
    private CreneauHoraire creneau;

    public Seance(String id, Groupe groupe, Enseignant enseignant, Cours cours,
                  String salle, String date, String heureDebut, String heureFin) {
        this(id, groupe, enseignant, cours, salle,
                LocalDate.parse(date), LocalTime.parse(heureDebut), LocalTime.parse(heureFin));
    }

    // Keep original constructor for backward compatibility
    public Seance(Groupe groupe, Enseignant enseignant, Cours cours,
                  String salle, String date, String heureDebut, String heureFin) {
        this("", groupe, enseignant, cours, salle, date, heureDebut, heureFin);
    }

    public Seance(String id, Groupe groupe, Enseignant enseignant, Cours cours,
                  String salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.id = Objects.requireNonNull(id, "id");
        this.groupe = Objects.requireNonNull(groupe, "groupe");
        this.enseignant = Objects.requireNonNull(enseignant, "enseignant");
        this.cours = cours;
        this.salle = Objects.requireNonNull(salle, "salle");
        this.creneau = new CreneauHoraire(date, heureDebut, heureFin);
    }

    public String getId()              { return id; }
    public Groupe getGroupe()          { return groupe; }
    public Enseignant getEnseignant()  { return enseignant; }
    public Cours getCours()            { return cours; }
    public String getSalle()           { return salle; }

    // Time slot (date, heureDebut, heureFin)
    public CreneauHoraire getCreneau() { return creneau; }
    public LocalDate getDate()         { return creneau.getDate(); }
    public LocalTime getHeureDebut()   { return creneau.getHeureDebut(); }
    public LocalTime getHeureFin()     { return creneau.getHeureFin(); }

    // Backward-compatible aliases used by the UI
    public String getJour()  { return getDate().toString(); }
    public String getDebut() { return getHeureDebut().toString(); }
    public String getFin()   { return getHeureFin().toString(); }

    public void setId(String id)       { this.id = id; }
    public void setSalle(String salle) { this.salle = salle; }

    public String getGroupeId()        { return groupe != null ? groupe.getGroupId() : "(aucun)"; }
    public String getEnseignantName()  { return enseignant != null ? enseignant.getName() : "(aucun)"; }
    public String getCoursTitle()      { return cours != null ? cours.getTitle() : "(aucun)"; }

    /**
     * Returns true if this seance overlaps with another on the same day.
     */
    public boolean overlapsWith(Seance other) {
        return this.creneau.chevauche(other.creneau);
    }

    @Override
    public String toString() {
        return id + " | " + getJour() + " " + getDebut() + "-" + getFin()
             + " | " + getCoursTitle()
             + " | Groupe: " + getGroupeId()
             + " | Salle: " + salle;
    }
}
