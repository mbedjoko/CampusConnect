package core.actors;

import java.util.ArrayList;
import java.util.List;
import core.courses.Cours;
import core.exceptions.CapaciteDepasseeException;

public class Etudiant extends Personne {
    private String studentNumber;
    private List<Cours> coursInscrits;

    public Etudiant(int id, String name, String email, String studentNumber) {
        super(id, name, email);
        this.studentNumber = studentNumber;
        this.coursInscrits = new ArrayList<>();
    }

    public String getStudentNumber()     { return studentNumber; }
    public List<Cours> getCoursInscrits(){ return coursInscrits; }

    public void inscrire(Cours cours) throws CapaciteDepasseeException {
        if (cours.isFull()) {
            throw new CapaciteDepasseeException(
                "Le cours « " + cours.getTitle() + " » est complet.");
        }
        if (!coursInscrits.contains(cours)) {
            coursInscrits.add(cours);
            cours.addEtudiant(this);
        }
    }

    @Override
    public String toString() { return id + " – " + name + " (" + studentNumber + ")"; }
}