package core.actors;

import java.util.ArrayList;
import java.util.List;
import core.courses.Cours;

public class Enseignant extends Personne {
    private String employeeId;
    private List<Cours> assignedCourses;

    public Enseignant(int id, String name, String email, String employeeId) {
        super(id, name, email);
        this.employeeId = employeeId;
        this.assignedCourses = new ArrayList<>();
    }

    public String getEmployeeId()           { return employeeId; }
    public List<Cours> getAssignedCourses() { return assignedCourses; }

    public void assignCourse(Cours cours) {
        if (!assignedCourses.contains(cours)) {
            assignedCourses.add(cours);
        }
    }

    @Override
    public String toString() { return id + " – " + name + " (" + employeeId + ")"; }
}