package core.actors;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import core.courses.Cours;

    /**
     * represente un enseignant dans le systeme
     * <p>
     * un enseignant est caracterisé par son statut(Permanent ou Vacataire)
     * et son departement de rattachament. il assure la responsabilité de
     * plusieurs enseignements
     * </p>
     * 
     * @author Mondo Daniel
     * @version 1.0
     */

public class Enseignant extends Personne {

    private String statut; // permanent ou vacataire
    private String departement;
    private Set<Cours> coursEnseignes;

    /**
     * construit un nouvel enseignant avec ses informations personnelles et professionnelles
     * 
     * @param nom Nom de famille
     * @param prenom Prenom de l'enseignant
     * @param adresseMail Adresse Mail
     * @param dateNaissance Date de naissance
     * @param statut (permanent ou vacataire)
     * @param departement Departement de rattachement
     */
    public Enseignant(String nom, String prenom, String adresseMail, LocalDate dateNaissance,
                      String statut, String departement) {
        super(nom, prenom, adresseMail, dateNaissance);
        this.statut = statut;
        this.departement = departement;
        this.coursEnseignes = new HashSet<>();
    }

    /**
     * ajoute un cours a la liste des enseignements dispenses par un enseignant
     * <p>verifie si le cours n'est pas deja present pour eviter les doublons</p>
     * 
     * @param cours le cours a ajouter
     */
    public void addCourse(Cours cours) {
        if (cours != null) {
            this.coursEnseignes.add(cours);
        }
    }
    

    /**
     * methode d'affichage pour inclure les details professionnels et la trace des enseignements
     * <p>affiche le role, l'identite complete, le statut, le departement et
     * la trace des enseignements</p>
     */
    @Override
    public void afficherDetails() {
        System.out.println("[ENSEIGNANT] [" + id + "] " + nom + " " + prenom
                + " | Adresse Mail: " + adresseMail
                + " | Date de Naissance: " + dateNaissance
                + " | Statut : " + statut
                + " | Departement : " + departement);
        if (coursEnseignes.isEmpty()) {
            System.out.println("  Cours Dispenses : Aucun");
        } else {
            System.out.print("  Cours Dispenses : ");
            for (Cours c : coursEnseignes) {
                System.out.print(c + " ; ");
            }
            System.out.println();
        }
    }

    /**
     * retourne une representation lisible de l'enseignant
     * utilise par les ComboBox JavaFX et les logs
     */
    @Override
    public String toString() {
        return nom + " " + prenom + " (" + statut + ")";
    }

    /** @return le statut de l'enseignant */
    public String getStatut() { return statut; }

    /** @return le departement de rattachement */
    public String getDepartement() { return departement; }

    /** @return la liste des cours dispenses */
    public List<Cours> getCoursEnseignes() {
        return new ArrayList<>(coursEnseignes);
    }
}