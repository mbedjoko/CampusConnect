package core.group;

import java.util.ArrayList;
import java.util.List;
import core.actors.Enseignant;
import core.actors.Etudiant;
import core.exceptions.CapaciteDepasseeException;

public class Groupe {
    private String groupId;
    private int capaciteMax;
    private List<Etudiant> etudiants;
    private Enseignant enseignant;

    public Groupe(String groupId, Enseignant enseignant) {
        this.groupId = groupId;
        this.enseignant = enseignant;
        this.capaciteMax = 30; // default
        this.etudiants = new ArrayList<>();
    }

    public Groupe(String groupId, int capaciteMax, Enseignant enseignant) {
        this.groupId = groupId;
        this.capaciteMax = capaciteMax;
        this.enseignant = enseignant;
        this.etudiants = new ArrayList<>();
    }

    public String getGroupId()            { return groupId; }
    public int getCapaciteMax()           { return capaciteMax; }
    public List<Etudiant> getEtudiants()  { return etudiants; }
    public Enseignant getEnseignant()     { return enseignant; }
    public int getNbEtudiants()           { return etudiants.size(); }

    public void setCapaciteMax(int cap)        { this.capaciteMax = cap; }
    public void setEnseignant(Enseignant e)    { this.enseignant = e; }

    public String getEnseignantName() {
        return enseignant != null ? enseignant.getName() : "(aucun)";
    }

    public boolean isFull() { return etudiants.size() >= capaciteMax; }

    public void addEtudiant(Etudiant etudiant) throws CapaciteDepasseeException {
        if (isFull()) {
            throw new CapaciteDepasseeException(
                "Le groupe « " + groupId + " » est plein (" + capaciteMax + "/" + capaciteMax + ").");
        }
        if (!etudiants.contains(etudiant)) {
            etudiants.add(etudiant);
        }
    }

    public void removeEtudiant(Etudiant etudiant) {
        etudiants.remove(etudiant);
    }

    @Override
    public String toString() { return groupId + " (" + getNbEtudiants() + "/" + capaciteMax + ")"; }
}