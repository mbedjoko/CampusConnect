package core.actors;

import java.time.LocalDate;

    /**
     * classe abstraite qui represente une personne dans le systeme
     * <p>
     * elle englobe les informations communs(nom, prenom, email) d'une personne(etudiant et enseignant)
     * elle ne peut pas etre instancié directement
     * </p>
     * 
     * @author Mondo Daniel
     * @version 1.0
     */

public abstract class Personne {

    /** compteur statique pour generer automatiquement les identifiants uniques */
    private static int counter = 0;

    protected String id;
    protected String nom;
    protected String prenom;
    protected String adresseMail;
    protected LocalDate dateNaissance;


    /**
     * construit une nouvelle personne et genere automatiquement son
     * identifiant grace au compteur statique
     * 
     * @param nom Nom de famille d'une personne
     * @param prenom Prenom d'une personne
     * @param adresseMail Adresse email avec un "@"
     * @param dateNaissance Date de naissance d'une personne
     */
public Personne(String nom, String prenom, String adresseMail, LocalDate dateNaissance) {
        this.id = "ID-" + (++counter);
        this.nom = nom;
        this.prenom = prenom;
        setEmail(adresseMail); //setter pour valider
        this.dateNaissance = dateNaissance;
    }


    /**
     * retourne le nom complet de la personne
     * <p>cette methode facilite l'integration avec les modules externes</p> 
     */
    
    /**@return une chaine de caractere contenant le nom et le prenom */
    public String getName() {
        return this.nom + " " + this.prenom;
    }

    /**@return l'identifiant unique de la personne */
    public String getId() { 
        return id;
    }

    /**@return le nom de famille */
    public String getNom() {
        return nom;
    }

    /**@return le prenom */
    public String getPrenom() {
        return prenom;
    }

    /**@return l'adresse mail */
    public String getAdresseMail() {
        return adresseMail;
    }
    
    /**@return date de naissance */
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }


    /**
     * modifie l'adresse mail apres verification du format
     * 
     * @param adresseMail la nouvelle adresse email
     * @throws IllegalArgumentException si l'adresse ne contient pas "@"
     */
public void setEmail(String adresseMail) {
        if (adresseMail != null && adresseMail.contains("@")) {
            this.adresseMail = adresseMail;
        } else {
            throw new IllegalArgumentException("Format Invalide : l'adresse doit contenir un '@'");
        }
    }


    /**
     * affiche les details d'une personne
     * <p>cette methode doit etre implementée par les sous classes
     * pour afficher les informations propre a leur role (etudiant ou enseignant)</p>
     */
public abstract void afficherDetails();

}