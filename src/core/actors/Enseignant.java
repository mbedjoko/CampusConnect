package core.actors;

import java.util.List;
import core.courses.Cours;

public class Enseignant extends Personne {
    private String employeeId;
    private List<Cours> assignedCourses;

    public Enseignant(int id, String name, String email, String employeeId) {
        super(id, name, email);
        this.employeeId = employeeId;
    }

    public void assignCourse(Cours cours) {
        // à implémenter
    }
}