package core.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gestionnaire centralisé des acteurs du systeme
 * <p>
 * cette classe sert a stocker et manipuler l'ensemble des personnes du systeme
 * elle utilise le polymorphisme(List) pour traiter les differents types d'acteurs et
 * une HashMap pour l'optimisation des recherhces
 * </p>
 * 
 * @author Mondo Daniel
 * @version 1.0
 */

public class GestionnaireActeurs {
    
    /**
     * ici sont stockés tout les acteurs de Campus-Connect
     * l'utilisation de la classe personne permet de stocker indifferement les etudiant et enseignants
     */
    private List<Personne> listePersonnes;

    /**
     * indexation des etudiants par leur matricule pour une recherche instantané
     * clé : Matricule (String), valeur : objet Etudiant
     */
    private Map<String, Etudiant> indexEtudiants;

    /**
     * initialise une HashMap et un nouveau gestionnaire
     */
    public GestionnaireActeurs() {
        this.listePersonnes = new ArrayList<>();
        this.indexEtudiants = new HashMap<>();
    }

    /**
     * ajoute une nouvelle personne au registre de l'universite
     * 
     * @param p la personne (etudiant ou enseignant) a ajouter
     */
    public void ajouterPersonne(Personne p) {
        if (p != null) {
            listePersonnes.add(p);

            //si la personne est un etudiant on l'ajoute dans la HashMap
            if (p instanceof Etudiant) {
                Etudiant e = (Etudiant) p;
                this.indexEtudiants.put(e.getMatricule(), e);
            }
        } 
    }

    /**
     * recherche un etudiant par son matricule
     * <p>
     * cette methode parcourt la liste globale, filtre les objets de types etudiant et compare les matricules
     * </p>
     * 
     * @param matricule le matricule a rechercher
     * @return l'objet etudiant correspondant ou null si aucun etudiant n'est trouvé
     */
    public Etudiant trouverEtudiant(String matricule) {
        if (matricule == null) return null;
        return this.indexEtudiants.get(matricule); //utilisation de HashMap
    }

    /**
     * affiche la liste complete des acteurs dans le systeme
     * <p>
     * cette methode illustre le concept de POLYMORPHISME : elle 
     * appelle la methode <code>afficherInfos()</code> sur chaque oubjet personne
     * et java execute automatiquement la version approprié (etudiant ou enseignant)
     * </p>
     */
    public void afficherToutLeMonde() {
        System.out.println("\n* LISTE DES ACTEURS DU CAMPUS");
        if (listePersonnes.isEmpty()) {
            System.out.println("(Aucun acteur enregistree)");
        } else {
            for (Personne p : listePersonnes) {
            p.afficherDetails();
        }
        }
        System.out.println("********************\n");
    }

    /**
     * retourne la liste de toutes les personnes enregistrées
     * 
     * @return une liste qui contient tout les objets Personne
     */
    public List<Personne> getListePersonnes() {
        return listePersonnes;
    }
}