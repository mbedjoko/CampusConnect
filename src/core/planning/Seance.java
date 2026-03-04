package core.planning;

import core.group.Groupe;
import core.actors.Enseignant;
import core.courses.Cours;

public class Seance {
    private Groupe groupe;
    private Enseignant enseignant;
    private Cours cours;
    private String salle;
    private String jour;
    private String debut;
    private String fin;

    public Seance(Groupe groupe, Enseignant enseignant, Cours cours, String salle, String jour, String debut, String fin) {
        this.groupe = groupe;
        this.enseignant = enseignant;
        this.cours = cours;
        this.salle = salle;
        this.jour = jour;
        this.debut = debut;
        this.fin = fin;
    }
}