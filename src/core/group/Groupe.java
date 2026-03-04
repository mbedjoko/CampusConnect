package core.group;

import java.util.List;
import core.actors.Etudiant;
import core.actors.Enseignant;

public class Groupe {
    private String groupId;
    private List<Etudiant> etudiants;
    private Enseignant enseignant;

    public Groupe(String groupId, Enseignant enseignant) {
        this.groupId = groupId;
        this.enseignant = enseignant;
    }

    public void addEtudiant(Etudiant etudiant) {
        etudiants.add(etudiant);
    }

    public void removeEtudiant(Etudiant etudiant) {
        etudiants.remove(etudiant);
    }
}