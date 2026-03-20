package core.actors;

import java.time.LocalDate;
import core.courses.Cours;
import core.group.Groupe;
import core.exceptions.*;

public class TestActeurs {

    public static void main(String[] args) {

        GestionnaireActeurs gestionnaire = new GestionnaireActeurs();


        //creation des acteurs
        Etudiant e1 = null;
        Etudiant e2 = null;
        Enseignant prof1 = null;

        try {
        e1 = new Etudiant("Kamga", "Dylane", "freddylane@gmail.com", LocalDate.of(2010,12,26), "22I01559", "Niveau 1", "Genie Logiciel");
        e2 = new Etudiant("Eboko", "Jacob", "jaceboko@gmail.com", LocalDate.of(2014,12,6), "22I02000", "Niveau 3", "Reseau Telecom");
        prof1 = new Enseignant("Lome", "Durand", "marcdurand@gmail.com", LocalDate.of(1990,3,30), "Vacataire", "Genie Informatique");
        
        gestionnaire.ajouterPersonne(e1);
        gestionnaire.ajouterPersonne(e2);
        gestionnaire.ajouterPersonne(prof1);
        } catch (IllegalArgumentException ex) {
            System.err.println("ERREUR :" + ex.getMessage());
            return;
        }


        //Simulation d'un cours et d'un groupe (via les classes du chef de projet : MEKEME)
        Cours javaCours = new Cours("INFO-301", "Progra Oriente Objet", 20, prof1);
        Groupe td1 = new Groupe("TD POO 1", prof1);

        //attribue un cours a l'enseignant (garder la trace de ses enseignements/ les cours enseignés)
        //prof1.addCourse(javaCours);

        //inscription et gestion des notes des etudiants
        Inscription ins1 = new Inscription(e1, td1);
        Inscription ins2 = new Inscription(e2, td1);
        e1.addInscription(ins1);
        e2.addInscription(ins2);

        System.out.println("\n*** TEST DE ROBUSTESSE : Ajout de notes ***");
        

        try {
            ins1.addNote(new Note(15.5, 2)); //Note valide
            ins1.addNote(new Note(12.5, 2)); //Note valide
            ins2.addNote(new Note(18.0, 3)); //Note valide
            System.out.println("\n* Notes Ajoutes avec Succes");

            //Test erreur note negative
            System.out.println("Tentative d'ajout d'une note negative");
            //ins1.addNote(new Note(-5, 2));
            ins2.addNote(new Note(-3, 2));
        } catch (NoteInvalideException ex) {
            System.err.println("\n -------ERREUR CAPTUREE : " + ex.getMessage());
        }


        // calcul des moyennes
        try {
            double moy1 = ins1.calculerMoyenne();
            System.out.println("\nMoyenne de " + e1.getNom() + " " + e1.getPrenom() + " en SI : " + moy1 + "/20");
            double moy2 = ins2.calculerMoyenne();
            System.out.println("Moyenne de " + e2.getNom() + " " + e2.getPrenom() + " en Programmation Web : " + moy2 + "/20");
        } catch (MoyenneIndisponibleException ex) {
            System.err.println(ex.getMessage());
        }
            System.out.println("********************");


        //test du polymorphisme
        System.out.println("\n*** TEST DU POLYMORPHISME (Affichage Global) ***");
        gestionnaire.afficherToutLeMonde();


        //Test du releve de notes global
        System.out.println("\n*** GENERATION DU RELEVE DE NOTES GLOBAL ***");
        System.out.println(e1.genererReleve());
        System.out.println(e2.genererReleve());

        //Test d'affichage de la trace des enseignements
        System.out.println("\n* TRACE DES ENSEIGNEMENTS");
        prof1.addCourse(javaCours);
        prof1.afficherDetails();
        System.out.println("********************");
    }
    
}