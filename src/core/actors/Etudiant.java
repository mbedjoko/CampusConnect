package core.actors;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import core.exceptions.MoyenneIndisponibleException;

    /**
     * represente un etudiant dans le systeme
     * <p>
     * etudiant est une personne qui possede un matricule, niveau d'etude
     * une filiere specifique et une liste d'inscriptions 
     * lui permettant de suivre ses cours et ses notes
     * </p>
     * 
     * @author Mondo Daniel
     * @version 1.0
     */

public class Etudiant extends Personne {

    private String matricule;
    private String anneeEtude;
    private String filiere;
    private List<Inscription> inscriptions;


    /**
     * construit un nouvel etudiant avec ses informations personnelles et academiques
     * 
     * @param nom Nom de famille
     * @param prenom Prenom de l'etudiant
     * @param adresseMail Adresse email
     * @param dateNaissance Date de naissance
     * @param matricule Matricule de l'etudiant
     * @param anneeEtude Annee ou niveau d'etude
     * @param filiere Filiere d'etude
     */
public Etudiant(String nom, String prenom, String adresseMail, LocalDate dateNaissance, String matricule, String anneeEtude, String filiere) {
        super(nom, prenom, adresseMail, dateNaissance);
        this.matricule = matricule;
        this.anneeEtude = anneeEtude;
        this.filiere = filiere;
        this.inscriptions = new ArrayList<>();
    }


    /**
     * ajoute une nouvelle inscritpion a la liste de l'etudiant
     * 
     * @param ins l'objet inscription a ajouter
     */
public void addInscription(Inscription ins) {
        if (ins != null){
            this.inscriptions.add(ins);
        }
    }


    /**
     * calcul la moyenne generale de l'etudiant sur l'ensemble de ses cours
     * <p>
     * cette methode parcourt toutes les inscriptions et ignore celles
     * qui ne possedent pas encore de notes
     * </p>
     * 
     * @return la moyenne generale. retourne 0.0 si aucune note n'est disponible
     */
public double calculerMoyenneGenerale() {
        double total = 0;
        int count = 0;
        for (Inscription ins : inscriptions) {
            try {
                total += ins.calculerMoyenne();
                count++;
            } catch (MoyenneIndisponibleException e) {

            }
        }
        return count == 0 ? 0.0 : total / count;
    }


    /**
     * genere le releve de notes complet de l'etudiant
     * 
     * @return une chaine de caracteres qui represente le bulettin
     */
public String genererReleve() {
        
        StringBuilder sb = new StringBuilder();

            sb.append("\n* RELEVE DE NOTES GLOBAL");
            sb.append("\n");
            sb.append("ETUDIANT : ").append(this.getName());
            sb.append("\n");
            sb.append("MATRICULE : ").append(this.getMatricule());
            sb.append("\n");
            sb.append("FILIERE : ").append(this.getFiliere());
            sb.append("\n");

        for (Inscription ins : inscriptions) {
            sb.append("GROUPE : ").append(ins.getGroupe().toString());
            sb.append("\n");
            sb.append("MATIERE : ").append(ins.getGroupe().toString());
            sb.append("\n");
            sb.append("NOTES : ");

        for (Note n : ins.getNotes()) {
            sb.append(n.getValeur()).append(" (coeff ").append(n.getCoefficient()).append(") | ");
            }

        try {
                double moyMatiere = ins.calculerMoyenne();
            sb.append("\n");
            sb.append("\n>> Moyenne Matiere : ").append(String.format("%.2f", moyMatiere)).append("/20");
        } catch (MoyenneIndisponibleException e) {
            sb.append(">> Moyenne Matiere : Pas de notes enregistree");
        }

        }
            double moyenneGenerale = calculerMoyenneGenerale();
            sb.append("\n");
            sb.append(">>> Moyenne Generale : ").append(String.format("%.2f", moyenneGenerale)).append("/20");
            sb.append("\n");
            sb.append("********************");

        return sb.toString();
    }


    /**
     * methode d'affichage pour inclure les details academiques
     * <p>affiche le role, l'identite complete, le matricule et la filiere</p>
     * 
     * @return le matricule de l'etudiant
     * @return la liste d'inscriptions
     * @return l'ennee d'etude actuelle
     * @return la filiere
     */
    @Override
public void afficherDetails() {
        System.out.println("[ETUDIANT] [" + id + "] " + nom + " " + prenom + " | Adresse Mail: " + adresseMail + " | Date de Naissance: " + dateNaissance + " | Matricule: " + matricule + " | Niveau: " + anneeEtude + " | Filiere: " + filiere);
    }
    public String getMatricule() {
        return matricule;
    }
    public List<Inscription> getInscriptions() {
        return inscriptions;
    }
    public String getAnneeEtude() {
        return anneeEtude;
    }
    public String getFiliere() {
        return filiere;
    }
}