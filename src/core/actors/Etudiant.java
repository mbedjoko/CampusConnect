package core.actors;

import java.util.ArrayList;
import java.util.List;
//import core.courses.Cours;
import core.exceptions.CapaciteDepasseeException;
import java.time.LocalDate; //ajout
import core.exceptions.MoyenneIndisponibleException; //ajout

public class Etudiant extends Personne {
    private String matricule;
    //private List<Cours> coursInscrits;
    private String anneeEtude;
    private String filiere;
    private List<Inscription> inscriptions;

    public Etudiant(String nom, String prenom, String email, String matricule, LocalDate dateNaissance, String anneeEtude, String filiere) {
        super(nom, prenom, email, dateNaissance);
        this.matricule = matricule;
        this.anneeEtude = anneeEtude;
        this.filiere = filiere;
        this.inscriptions = new ArrayList<>();
    }

    public void addInscription(Inscription ins) {
        this.inscriptions.add(ins);
    }

    public double calculerMoyenneGenerale() {
        double totel = 0;
        int count = 0;
        for (Inscrpition ins : inscriptions) {
            try {
                total += ins.caldulateAverage();
                count++;
            } catch (MoyenneIndisponibleException e) { //see
                //matieres sans notes pour calcul general
            }
        }
        return count == 0 ? 0.0 : total / count;
    }


    @Override
    public void afficherInfos() {
        System.out.println("[ETUDIANT]" + nom + "" + prenom + " | Matricule:" + matricule + "| Filiere:" + filiere);
    }

    public String getMatricule() {return matricule;}
    public List<Inscription> getInscriptions() {return inscriptions;}
}