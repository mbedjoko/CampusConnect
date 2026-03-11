package core.actors;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import core.courses.Cours; // Stub

public class Enseignant extends Personne {
    private String statut; // permanent  vacataire
    private String departement;
    private List<Cours> coursEnseignes;

    public Enseignant(String nom, String prenom, String email, String statut, LocalDate dateNaissance, String departement) {
        super(name, prenom, email, dateNaissance);
        this.statut = statut;
        this.departement = departement;
        this.coursEnseignes = new ArrayList<>();
        }

    public String getStatut()           { return statut; }
    public List<Cours> getCoursEnseignes() { return coursEnseignes; }

    public void addCourse(Cours cours) {
        if (!coursEnseignes.contains(cours)) {
            coursEnseignes.add(cours);
        }
    }

    @Override
    public void afficherInfos() {
        System.out.println("[ENSEIGNANT]" + nom + "" + prenom + "| Statut:" + statut + "| Departement:" + departement);
    }
}