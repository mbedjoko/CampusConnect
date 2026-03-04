package core.courses;

import java.util.List;
import core.group.Groupe;
import core.actors.Etudiant;
import core.actors.Enseignant;
import core.exceptions.CapaciteDepasseeException;

public class Cours {
    private String code;
    private String title;
    private int capacite;
    private Enseignant enseignant;
    private List<Groupe> groupes;

    public Cours(String code, String title, int capacite, Enseignant enseignant) {
        this.code = code;
        this.title = title;
        this.capacite = capacite;
        this.enseignant = enseignant;
    }

    public boolean isFull() {
        // à implémenter
        return false;
    }

    public void addEtudiant(Etudiant etudiant) throws CapaciteDepasseeException {
        // à implémenter
    }
}