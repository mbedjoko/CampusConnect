package core.actors;

import java.util.ArrayList;
import java.util.List;
import core.exceptions.MoyenneIndisponibleException;
import core.group.Groupe;

    /**
     * represente l'inscription d'un etudiant a un groupe de cours specifique
     * <p>
     * elle centralise les notes obtenues par l'etudiant pour ce groupe et permet de calculer la moyenne ponderée associé
     * cette classe sert de lien entre l'etudiant et l'offre de formation (groupe)
     * </p>
     * 
     * @author Mondo Daniel
     * @version 1.0
     */

public class Inscription {
    private Etudiant etudiant;
    private Groupe groupe;
    private List<Note> notes;


    /**
     * crée une nouvelle inscription pour un étudiant dans un groupe 
     * 
     * @param etudiant l'etudiant a inscrire 
     * @param groupe le groupe de cours cible 
     */
public Inscription(Etudiant etudiant, Groupe groupe) {
        this.etudiant = etudiant;
        this.groupe = groupe;
        this.notes = new ArrayList<>();
    }


    /**
     * ajoute une note a la liste des evaluations de cette inscription
     * 
     * @param note la note a ajouter
     */
public void addNote(Note note) {
    if (note != null) {
         this.notes.add(note);
    }
}


    /**
     * calacule la moyenne ponderée de l'etudiant pour cette inscription
     * <p>
     * la moyenne est calculé en faisant la somme des (notes * coefficients)
     * divisé par la somme des coefficients
     * </p>
     * 
     * @return la moyenne ponderée
     * @return l'etudiant associé a cette inscription
     * @return le groupe de cours concerné
     * @return la liste des notes obtenues
     * @throws MoyenneIndisponibleException si la liste des notes est vide
     */

public double calculerMoyenne() throws MoyenneIndisponibleException {
        if (notes.isEmpty()) {
            throw new MoyenneIndisponibleException("calcul impossible : Aucune note enregistree pour le cours lie au groupe" + groupe.toString());
        }
        double sum = 0;
        double coeffSum = 0;
        for (Note n : notes) {
            sum += (n.getValeur() * n.getCoefficient());
            coeffSum += n.getCoefficient();
        }
        // pour eviter la division par 0 si les coeffs sont nuls
        if (coeffSum == 0) {
            return 0.0;
        }

        return sum / coeffSum;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }
    public Groupe getGroupe() {
        return groupe;
    }
    public List<Note> getNotes() {
        return notes;
    }
}