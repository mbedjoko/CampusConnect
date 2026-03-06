package core.planning;

import core.actors.Enseignant;
import core.courses.Cours;
import core.group.Groupe;

public class Seance {
    private String id;
    private Groupe groupe;
    private Enseignant enseignant;
    private Cours cours;
    private String salle;
    private String jour;
    private String debut;
    private String fin;

    public Seance(String id, Groupe groupe, Enseignant enseignant, Cours cours,
                  String salle, String jour, String debut, String fin) {
        this.id = id;
        this.groupe = groupe;
        this.enseignant = enseignant;
        this.cours = cours;
        this.salle = salle;
        this.jour = jour;
        this.debut = debut;
        this.fin = fin;
    }

    // Keep original constructor for backward compatibility
    public Seance(Groupe groupe, Enseignant enseignant, Cours cours,
                  String salle, String jour, String debut, String fin) {
        this("", groupe, enseignant, cours, salle, jour, debut, fin);
    }

    public String getId()              { return id; }
    public Groupe getGroupe()          { return groupe; }
    public Enseignant getEnseignant()  { return enseignant; }
    public Cours getCours()            { return cours; }
    public String getSalle()           { return salle; }
    public String getJour()            { return jour; }
    public String getDebut()           { return debut; }
    public String getFin()             { return fin; }

    public void setId(String id)       { this.id = id; }
    public void setSalle(String salle) { this.salle = salle; }

    public String getGroupeId()        { return groupe != null ? groupe.getGroupId() : "(aucun)"; }
    public String getEnseignantName()  { return enseignant != null ? enseignant.getName() : "(aucun)"; }
    public String getCoursTitle()      { return cours != null ? cours.getTitle() : "(aucun)"; }

    /**
     * Returns true if this seance overlaps with another on the same day.
     * Times are compared as strings in HH:mm format (lexicographic = correct for 24h).
     */
    public boolean overlapsWith(Seance other) {
        if (!this.jour.equals(other.jour)) return false;
        // overlap: this starts before other ends AND other starts before this ends
        return this.debut.compareTo(other.fin) < 0
            && other.debut.compareTo(this.fin) < 0;
    }

    @Override
    public String toString() {
        return id + " | " + jour + " " + debut + "-" + fin
             + " | " + getCoursTitle()
             + " | Groupe: " + getGroupeId()
             + " | Salle: " + salle;
    }
}