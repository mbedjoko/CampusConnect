package core.courses;

import java.util.ArrayList;
import java.util.List;
import core.actors.Enseignant;
import core.actors.Etudiant;
import core.exceptions.CapaciteDepasseeException;
import core.group.Groupe;

public class Cours {
    private String code;
    private String title;
    private int capacite;
    private Enseignant enseignant;
    private List<Groupe> groupes;
    private List<Etudiant> etudiants;

    public Cours(String code, String title, int capacite, Enseignant enseignant) {
        this.code = code;
        this.title = title;
        this.capacite = capacite;
        this.enseignant = enseignant;
        this.groupes = new ArrayList<>();
        this.etudiants = new ArrayList<>();
    }

    public String getCode()              { return code; }
    public String getTitle()             { return title; }
    public int getCapacite()             { return capacite; }
    public Enseignant getEnseignant()    { return enseignant; }
    public List<Groupe> getGroupes()     { return groupes; }
    public List<Etudiant> getEtudiants() { return etudiants; }

    public void setCode(String code)             { this.code = code; }
    public void setTitle(String title)           { this.title = title; }
    public void setCapacite(int capacite)        { this.capacite = capacite; }
    public void setEnseignant(Enseignant e)      { this.enseignant = e; }

    public String getEnseignantName() {
        return enseignant != null ? enseignant.getName() : "(aucun)";
    }

    public boolean isFull() {
        return etudiants.size() >= capacite;
    }

    public void addEtudiant(Etudiant etudiant) throws CapaciteDepasseeException {
        if (isFull()) {
            throw new CapaciteDepasseeException(
                "Capacité maximale atteinte pour le cours « " + title + " » (" + capacite + " places).");
        }
        if (!etudiants.contains(etudiant)) {
            etudiants.add(etudiant);
        }
    }

    public void addGroupe(Groupe groupe) {
        if (!groupes.contains(groupe)) {
            groupes.add(groupe);
        }
    }

    @Override
    public String toString() { return code + " – " + title; }
}