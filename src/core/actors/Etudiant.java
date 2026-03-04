package core.actors;

import java.util.List;
import core.courses.Cours;

public class Etudiant extends Personne {
    private String studentNumber;
    private List<Cours> coursInscrits;

    public Etudiant(int id, String name, String email, String studentNumber) {
        super(id, name, email);
        this.studentNumber = studentNumber;
    }

    public void inscrire(Cours cours) {
        // à implémenter avec vérification de capacité
    }
}